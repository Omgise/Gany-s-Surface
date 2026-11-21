# Gany's Surface — GTNH port

Unofficial port of [Gany's Surface](https://github.com/buttilda/Gany-s-Surface) 1.12.8
(Minecraft 1.7.10) to the GTNH build environment, with the content that
[Et Futurum Requiem](https://github.com/GTNewHorizons/Et-Futurum-Requiem) already
provides removed.

This branch (`gtnh`) is a personal-use fork. It is not affiliated with the GTNH
team and is not published to CurseForge/Modrinth.

## Build environment

| Item | Value |
|---|---|
| Minecraft / Forge | 1.7.10 / 10.13.4.1614 |
| MCP | channel `stable`, version `12` |
| Build framework | RetroFuturaGradle via `com.gtnewhorizons.gtnhconvention` 2.0.33 |
| Gradle | 9.7.1 (wrapper) |
| JDK | Gradle daemon 25, toolchains 17/21 (Azul Zulu), game runtime 8 |

```bat
:: from the repository root
gradlew.bat build
```

Artifacts land in `build/libs/`:

| File | Purpose |
|---|---|
| `ganyssurface-1.12.8.jar` | release build (SRG names), drop this into `mods/` |
| `ganyssurface-1.12.8-dev.jar` | development build (MCP names), for `runClient` |
| `ganyssurface-1.12.8-sources.jar` | sources |

Optional integrations are compiled against GTNH Maven artifacts and are only wired
up at runtime when the matching mod is installed:

* CodeChickenCore `1.4.22` + NotEnoughItems `2.7.69-GTNH` (NEI recipe handlers)
* Thaumcraft `1.7.10-4.2.3.5` (aspect registration)

`Spotless` and `Checkstyle` are disabled in `gradle.properties`. The upstream code
base is tab-indented with a different import order; running `spotlessApply` would
reformat roughly 300 files and bury the actual port changes. Enable them (and run
`gradlew spotlessApply` once) if this fork ever becomes the main line of
development.

## Removed content

Everything below used to be registered by Gany's Surface *and* is also registered
by Et Futurum Requiem 2.7.3-GTNH. Keeping both would put two blocks/items with the
same look and purpose in a GTNH instance, so the Gany's Surface copy was removed
together with its recipes, ore dictionary entries, language keys, textures and
config options.

| Removed from Gany's Surface | Et Futurum Requiem equivalent |
|---|---|
| `18Stones` (granite / diorite / andesite + polished, incl. world gen) | `granite`, `diorite`, `andesite` (+ polished, stairs, slabs, walls) |
| `iron_trapdoor` | `iron_trapdoor` |
| `prismarineBlocks` (prismarine / bricks / dark) | `prismarine`, `prismarine_bricks`, `dark_prismarine` |
| `sea_lantern` | `sea_lantern` |
| `daylight_detector_inverted` | `daylight_detector_inverted` |
| `red_sandstone` + slab + stairs | `red_sandstone`, `red_sandstone_slab`, `red_sandstone_stairs` |
| `coarse_dirt` (+ world gen conversion) | `coarse_dirt` |
| `banner` (all 16 colours, patterns, tile entity, renderer, NEI handler) | `banner` |
| `sponge` / wet sponge | `sponge`, `wet_sponge` |
| `slimeBlock` | `slime` |
| `beetrootBlock` + beetroot / seeds / soup | `beetroots` + `beetroot`, `beetroot_seeds`, `beetroot_soup` |
| raw / cooked mutton (sheep drops, smelting) | `mutton`, `cooked_mutton` |
| `prismarineItems` (shard / crystals) | `prismarine_shard`, `prismarine_crystals` |
| wood doors, fence gates, fences | `DOOR_*`, `FENCE_GATE_*`, `FENCE_*` |
| wood buttons, pressure plates, trapdoors, signs | `BUTTON_*`, `PRESSURE_PLATE_*`, `TRAPDOOR_*`, `SIGN_*` |
| silk-touch mushroom blocks | `brown_mushroom`, `red_mushroom` |
| `lantern` | `lantern` (both are light-emitting lantern blocks) |
| `basalt` / polished basalt (+ world gen) | `basalt`, `smooth_basalt` |
| ocean monument (built out of the removed prismarine/sea lantern blocks) | Et Futurum Requiem's own structure handling |

Kept as Gany's Surface content: tea/camellia, poop and rot, wooden armour, wooden
chests, wooden ladders, wood-specific bookshelves, work tables, market, planters,
dislocators, encasers, item display, ink harvester, rain detector, cushion,
chocolate cake, charcoal block, slow rail, coloured redstone, storage/dye blocks,
wooden wrench, quiver, paintings and every other non-1.8 feature.

Also dropped as a side effect of the 1.8 cleanup:

* the Tinkers' Construct smeltery recipes for the removed iron trapdoor;
* the 1.8-style "wood doors stack to 64 / vanilla door recipe replacement" tweaks
  (Et Futurum Requiem owns that behaviour now);
* Gany's Surface's 1.8 enchanting table GUI (lapis cost, per-player seed storage)
  and its `container.enchant.*` translations — the vanilla enchanting table is
  used again;
* the 1.8 stone brick recipes (mossy cobblestone / mossy stone brick from vines,
  chiselled stone brick from slabs, smooth stone brick from smelting).

## Upstream

Original mod by ganymedes01, released under the Unlicense. Forum thread:
<http://www.minecraftforum.net/topic/1933900-162-ganys-surface-v130-ganys-mods/>

## 中文说明

这个分支是把 Gany's Surface 1.12.8 迁移到 GTNH 构建环境（RFG + Gradle 9.7.1）后的
非官方版本，并删掉了所有与 Et Futurum Requiem 重复的方块与物品（见上表），包括它们的
配方、矿物词典条目、语言文件、贴图和配置项。迁移过程只改构建脚本和上面这些内容，其余
玩法逻辑保持上游原样。

构建：`gradlew.bat build`，产物 `build/libs/ganyssurface-1.12.8.jar`。
