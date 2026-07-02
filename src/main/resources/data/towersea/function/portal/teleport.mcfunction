# 给玩家添加冷却标记
tag @s add towersea_portal_cooldown
scoreboard players set @s towersea_cd 80
# 判断当前维度并传送
execute if dimension minecraft:overworld run function towersea:portal/enter_overworld_to_towersea
execute if dimension towersea:towersea run function towersea:portal/enter_towersea_to_overworld
