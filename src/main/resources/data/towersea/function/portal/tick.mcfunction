# 每刻检查所有传送门 marker
execute as @e[tag=towersea_portal_marker] run function towersea:portal/check_frame
# 检测玩家是否站在 light 方块上
execute as @a at @s if block ~ ~-0.5 ~ minecraft:light unless entity @s[tag=towersea_portal_cooldown] run function towersea:portal/teleport
