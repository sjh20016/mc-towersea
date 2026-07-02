# 冷却递减
scoreboard players remove @e[tag=towersea_magic_user] ts_cloud_wing_time 1
scoreboard players remove @e[tag=towersea_magic_user] ts_anchor_time 1
scoreboard players remove @e[tag=towersea_magic_user] ts_anchor_cd 1
scoreboard players remove @e[tag=towersea_magic_user] ts_mirror_cd 1
scoreboard players remove @e[tag=towersea_magic_user] ts_fall_seal_time 1
scoreboard players remove @e[tag=towersea_magic_user] ts_windshaft_cd 1
# 状态清理
execute as @a if entity @s[tag=towersea_magic_user] unless dimension towersea:towersea run tag @s remove towersea_magic_user
# Marker 寿命管理
execute as @e[tag=towersea_magic_marker] unless dimension towersea:towersea run kill @s
execute as @e[tag=towersea_magic_marker] at @s unless block ~ ~ ~ lapis_block run scoreboard players add @s ts_marker_time 1
execute as @e[tag=towersea_magic_marker] if score @s ts_marker_time matches 1200.. run kill @s
# 用户维护
execute as @e[tag=towersea_magic_user] unless scoreboard players @s ts_common_clock matches 0.. run tag @s remove towersea_magic_user
