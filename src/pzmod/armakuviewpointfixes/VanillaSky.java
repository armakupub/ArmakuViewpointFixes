package pzmod.armakuviewpointfixes;

import zombie.erosion.season.ErosionSeason;
import zombie.iso.weather.ClimateManager;
import zombie.GameTime;

// The game's light follows nightStrength, switching street lights at 0.5; Viewpoint's sun follows dusk and dawn.
public class VanillaSky {
    static final double DEEPEST = Math.toRadians(18.0);

    public static double hourAngle(double lat, double days, double hourAngle) {
        if (!Settings.VANILLA_SKY.get()) return hourAngle;
        ErosionSeason season = ClimateManager.getInstance().getSeason();
        if (season == null || season.getDaylight() <= 0.0f || season.getDaylight() >= 24.0f) return hourAngle;
        double dec = declination(days);
        double sunset = halfDay(lat, dec);
        double lowest = Math.asin(Math.sin(lat) * Math.sin(dec) - Math.cos(lat) * Math.cos(dec));
        if (sunset <= 0.0 || sunset >= Math.PI || lowest >= 0.0) return hourAngle;
        double deepest = Math.min(DEEPEST, -lowest);

        double night = 24.0 - season.getDaylight();
        double day = season.getDaylight() / 2.0 + night / 6.0;
        double x = GameTime.getInstance().getTimeOfDay() - season.getDayHighNoon();
        x -= 24.0 * Math.floor((x + 12.0) / 24.0);
        double from = Math.abs(x), h;
        if (from <= day) {
            h = sunset * from / day;
        } else {
            double after = from - day, twilight = night / 12.0;
            if (after < twilight) {
                double strength = Math.min(1.0, 1.0 - Math.cos(Math.PI * (night / 6.0 + after) / (night / 2.0)));
                h = below(lat, dec, -deepest * (2.0 * strength - 1.0));
            } else {
                double deep = below(lat, dec, -deepest);
                h = deep + (Math.PI - deep) * (after - twilight) / (night / 3.0 - twilight);
            }
        }
        return x >= 0.0 ? h : -h;
    }

    static double below(double lat, double dec, double elevation) {
        double cos = (Math.sin(elevation) - Math.sin(lat) * Math.sin(dec)) / (Math.cos(lat) * Math.cos(dec));
        return Math.acos(Math.max(-1.0, Math.min(1.0, cos)));
    }

    // Viewpoint's Ephemeris.
    static double declination(double days) {
        double g = Math.toRadians(357.529 + 0.98560028 * days);
        double longitude = Math.toRadians(280.459 + 0.98564736 * days + 1.915 * Math.sin(g) + 0.02 * Math.sin(2.0 * g));
        return Math.asin(Math.sin(Math.toRadians(23.439 - 3.6E-7 * days)) * Math.sin(longitude));
    }

    static double halfDay(double lat, double dec) {
        double cos = -Math.tan(lat) * Math.tan(dec);
        return cos >= 1.0 ? 0.0 : cos <= -1.0 ? Math.PI : Math.acos(cos);
    }
}
