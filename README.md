# Gamma-Hollow

This mod adds a new dimension that is ideal for mining. It features ~1000 blocks high terrain with ~800 blocks of a cave system with very dense ore distributions.

## Dimension

The dimension itself is described in **resources** like in a data pack. A few features were added with a Java implementation (such as the lava surface). Optional features are described with tags (for optional mod dependencies).

## Radiation

The radiation system is mostly described through Java and adds its own damage type and status effect. For each entity, it scans upwards and outwards for open access to the sky (transparent blocks are not allowed). While exposed to the sky, the status effect gets worse fives faster than it otherwise decays. Every five minutes, it moves up to the next level, until level four, where its timer keeps increasing. It also gives off blinding flashes.

## Other Features

Some other odd features were necessary. To light up the lush caves, glow vines were needed on leaves blocks, which required a "mimic" leaves block. Also, as most animals are not designed to spawn in caves, a custom spawner system was implemented.

## To-Do

A few features are to be added:
- A more vibrant sky box with a supernova.
- Custom creatures and monsters?
- Compatibility with other mods?