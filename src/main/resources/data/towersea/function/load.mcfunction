# [TowerSea] 天井塔海 v0.1 数据包加载
scoreboard objectives add towersea_rng dummy
scoreboard objectives add towersea_cd dummy
scoreboard objectives add towersea_y dummy
scoreboard objectives add towersea_state dummy
scoreboard objectives add towersea_bridge_ray dummy
scoreboard objectives add towersea_bridge_found dummy
scoreboard objectives add towersea_bridge_age dummy
scoreboard objectives add towersea_bridge_len dummy
scoreboard objectives add towersea_bridge_clear dummy
function towersea:magic/common/load
tellraw @a {"text":"[TowerSea] 天井塔海 v0.1 已加载","color":"aqua"}
