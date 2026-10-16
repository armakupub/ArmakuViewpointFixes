-- Shown as Viewpoint shows its loot window: unfolded, and put back as it was.
local inventoryOpen, inventoryWasHidden, inventoryFreed = false, false, false

local function onInventoryKey(key)
    if not ArmakuViewpointFixes.inventoryKey(key) then return end
    -- The key's game uses: the chat's streams and the furniture tool's mode.
    if ISChat and ISChat.focused then return end
    if getCell() and getCell():getDrag(0) then return end
    local inv = getPlayerInventory(0)
    if not inv then return end
    if inventoryOpen and inv:getIsVisible() then
        if inventoryWasHidden then
            inv:setVisible(false)
        elseif not inv.pin then
            inv:collapseNow()
        end
        if inventoryFreed then ArmakuViewpointFixes.holdCursor() end
        inventoryOpen, inventoryFreed = false, false
        return
    end
    inventoryWasHidden = not inv:getIsVisible()
    inv:setVisible(true)
    inv.isCollapsed = false
    inv:clearMaxDrawHeight()
    inv.collapseCounter = 0
    inventoryFreed = ArmakuViewpointFixes.freeCursor() or (inventoryOpen and inventoryFreed)
    inventoryOpen = true
end

local added = false

Events.OnGameStart.Add(function()
    if not ArmakuViewpointFixes or added then return end
    added = true
    Events.OnKeyStartPressed.Add(onInventoryKey)
end)
