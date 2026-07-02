# 水平桥：每刻建造 2 格宽桥段
setblock ~1 ~ ~ minecraft:oak_planks replace minecraft:air
setblock ~ ~ ~ minecraft:oak_planks replace minecraft:air
fill ~1 ~1 ~ ~ ~2 ~ minecraft:air
scoreboard players add @s towersea_bridge_len 1
execute if score @s towersea_bridge_len matches 30.. run function towersea:magic/bridge/finish
execute at @s unless block ~2 ~ ~ air run kill @s
