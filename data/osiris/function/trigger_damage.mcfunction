# 1. Generate the random damage and save it to storage
execute store result storage osiris:data current_damage int 1 run random value 1..10

# 2. CALL THE MACRO (The critical 'execute as @s' fix)
# This ensures that inside the macro, @s refers to the entity being damaged.
execute as @s run function osiris:apply_damage_macro with storage osiris:data

# Pick a random cooldown timer (e.g., 2s to 10s = 40 to 200 ticks)
# This prevents them from being hit again immediately
execute store result score @s radiation_timer run random value 40..200

# Optional: Debug message to confirm it's working
# execute as @s run tellraw @a [{"selector":"@s","color":"yellow"},{"text":" was hit by radiation! Next pulse in: ","color":"gray"},{"score":{"name":"@s","objective":"radiation_timer"},"color":"white"},{"text":" ticks."}]