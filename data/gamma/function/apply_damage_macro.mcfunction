# # This line will run even if the macro lines fail, helping us debug
# tellraw @a "--- Macro Function Attempted ---"

# # 1. DEBUG: Print context and storage value to chat
# # Using current_damage to match what we stored
# $tellraw @a ["",{"text":"[DEBUG] ","color":"red"},{"text":"Target: ","color":"gray"},{"selector":"@s","color":"yellow"},{"text":" | Damage Value: ","color":"gray"},{"text":"$(current_damage)","color":"gold"}]

# Deal damage to the entity based on the stored damage value
$damage @s $(current_damage) minecraft:generic

# Visual/Sound feedback
effect give @s minecraft:blindness 1 1 true
effect give @s minecraft:glowing 1 1 true
particle minecraft:flash ~ ~1.5 ~ 0.1 0.1 0.1 0 1