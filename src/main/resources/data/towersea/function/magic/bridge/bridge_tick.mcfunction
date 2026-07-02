# 驱动所有活跃桥 marker
execute as @e[tag=towersea_bridge_wait] run function towersea:magic/bridge/await_growth
execute as @e[tag=towersea_bridge_active] run function towersea:magic/bridge/step_forward
