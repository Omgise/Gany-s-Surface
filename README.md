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

Removed on request as well (mostly Gany's Surface's own content; the bed renderer is
an Et Futurum duplicate):

| Removed | Notes |
|---|---|
| disguised trapdoors (6 wood types) | trapdoors whose texture mimics a block |
| wood-specific chests (6 types) | `BlockWoodChest`, `TileEntityWoodChest`, its block/item/TESR renderers and the "dynamic chest texture" option |
| wood-specific ladders (5 types) | `BlockWoodLadder` |
| wood-specific sticks (5 types) | `ModItems.stick`, which existed to craft those ladders |
| wood-specific bookshelves (5 types) | `BlockWoodBookshelf`, textured per wood type |
| 3D bed inventory model | `ItemBedRenderer`; Et Futurum Requiem does the same with `Item3DBedRenderer` (`inventoryBedModels`) |
| 3D inventory models for vanilla items | `enable3DRendering`: hopper, cauldron, brewing stand, repeater, comparator, the five minecart items, boat, flower pot, lever, cake, torches and sign fell back to the vanilla 2D sprites; dropped because the extra renderers cost frame time. `ItemCakeRenderer` stays for Gany's own eaten/chocolate cake items |
| leaf walls (hedges, 4 wood types) | `LeafWall`, its recipes, Thaumcraft aspects and the "Enable leaf walls" option |

Their recipes, ore dictionary entries (`chestWood` / `stickWood` additions) and the
vanilla-recipe replacements they performed are gone too, so vanilla chest, ladder,
stick and bookshelf recipes behave exactly as in 1.7.10 again. That also removed
the last user of `GanysSurface.WOOD_NAMES`.

Kept as Gany's Surface content: tea/camellia, poop and rot, wooden armour, work
tables, market, planters, dislocators, encasers, item display, ink harvester, rain
detector, cushion, chocolate cake, charcoal block, slow rail, coloured redstone,
storage/dye blocks, wooden wrench, quiver, paintings and every other non-1.8
feature.

Also dropped as a side effect of the 1.8 cleanup:

* the Tinkers' Construct smeltery recipes for the removed iron trapdoor;
* the 1.8-style "wood doors stack to 64 / vanilla door recipe replacement" tweaks;
* Gany's Surface's 1.8 enchanting table GUI (it hijacked the vanilla enchanting
  table and added the lapis cost plus per-player seed storage) and its
  `container.enchant.*` translations — Et Futurum Requiem ships its own
  `etfuturum:enchanting_table` block with the same GUI (`GuiEnchantment`,
  `ContainerEnchantment`, `EnchantingFuelRegistry`), so the vanilla table simply
  stays vanilla;
* the 1.8 stone brick recipes (mossy cobblestone / mossy stone brick from vines,
  chiselled stone brick from slabs, smooth stone brick from smelting) — Et
  Futurum Requiem's `enableStoneBrickRecipes` option owns those now.

## Quiver

The quiver keeps plain arrows as a counter and stores Et Futurum Requiem's
tipped arrows as NBT stacks (they carry their potion), firing them through EF's
`EntityTippedArrow`. The tooltip lists the contents one line per type, using each
arrow's own display name so the client language applies; an empty quiver still
shows a zero line:

```
箭筒:
64 x 箭
64 x 反胃之箭
64 x 虚弱之箭
```

It implements Baubles-Expanded's `IBaubleExpanded` when that
mod is installed, accepting only the `body` (back/torso) slot — the one a default
Baubles-Expanded setup actually assigns — and the bow handlers also look for a
quiver worn as a bauble. Both integrations are optional at
runtime: EF is reached by reflection, the Baubles item class is only loaded when
its API class is present, and the check uses the class rather than `isModLoaded`
because the fork's mod id is `Baubles|Expanded`.

### Modded bows

Other mods' bows never call this mod. They ask `InventoryPlayer` instead:
`hasItem(Items.arrow)` decides whether they may draw and fire, and
`consumeInventoryItem(Items.arrow)` spends an arrow. `MixinInventoryPlayerQuiver`
answers both questions from the quiver, so a bow keeps its own arrow entity and
the quiver simply acts as its ammo:

| Bow | Result |
|---|---|
| vanilla bow | unchanged: the `ArrowLooseEvent` handler still fires the quiver's tipped arrows through Et Futurum's `EntityTippedArrow` |
| Twilight Forest (triple / ice / seeker / ender) | fires its own arrow; the event is no longer cancelled, so the triple shot, homing, freezing and ender teleport survive |
| Natura, Ender Zoo, CoFH bows, Mekanism's electric bow, RiftFlux's More Bows / Searing Bow, LegendGear | draw and fire with nothing but a quiver on the belt |
| Draconic Evolution | spends the quiver's plain arrows for its custom arrows; its own ender arrows are untouched |
| Avaritia's Infinity Bow | untouched (it does not consume arrows) |

Two rules are deliberate:

* **plain arrows only** — a modded bow spawns its own `EntityArrow`, so a tipped
  arrow taken from the quiver would silently lose its potion. Tipped arrows stay
  vanilla-bow ammo; a tipped-only quiver will not let a modded bow fire.
* **the inventory wins** — a real arrow stack in the inventory is always spent
  first; the quiver is only touched when there is none.

The quiver only ever sees inventory contents; bows that keep a private ammo model
(RiftFlux's World Explorer Long Bow, which reads the main inventory directly and
then its own quiver) are not routed through it.

Patching a vanilla class means the mod ships a mixin config and needs
**UniMixins** at runtime (every GTNH instance has it). `usesMixins = true` in
`gradle.properties` makes the GTNH convention add the manifest entries, the
UniMixins requirement and the refmap; the targets live in `MixinInventoryPlayerQuiver`.


While a quiver is worn it is drawn on the player's back, from the item's own icon,
so the empty art and the with-arrows art (texture packs such as Modernity draw the
latter at `minecraft:quiver`) always match the inventory. The sprite leans with the
torso pose, which carries it through vanilla sneaking and EvenMoBends' animated
body, and the cape is hidden while the quiver occupies that spot. Its placement is
a constant in `QuiverPlayerRenderer` (`SCALE` / `OFFSET_X` / `OFFSET_Y` / `OFFSET_Z`).

## Upstream

Original mod by ganymedes01, released under the Unlicense. Forum thread:
<http://www.minecraftforum.net/topic/1933900-162-ganys-surface-v130-ganys-mods/>

## 中文说明

这个分支是把 Gany's Surface 1.12.8 迁移到 GTNH 构建环境（RFG + Gradle 9.7.1）后的
非官方版本，并删掉了所有与 Et Futurum Requiem 重复的方块与物品（见上表），包括它们的
配方、矿物词典条目、语言文件、贴图和配置项。迁移过程只改构建脚本和上面这些内容，其余
玩法逻辑保持上游原样。

构建：`gradlew.bat build`，产物 `build/libs/ganyssurface-1.12.8.jar`。

**箭筒与其他模组的弓**：其他模组的弓不会调用本模组，它们只问
`InventoryPlayer.hasItem(Items.arrow)`（能不能拉弓/放箭）和
`consumeInventoryItem(Items.arrow)`（扣一支箭）。`MixinInventoryPlayerQuiver` 把这两个问题
接到箭筒上，所以暮色森林的三重弓/冰弓/追踪弓/末影弓、Natura、Ender Zoo、CoFH、Mekanism
电动弓、RiftFlux 的 More Bows / 灼热弓、LegendGear 都能只靠腰带上的箭筒拉弓放箭，而且各
自的特殊箭矢照常生效（原先 `ArrowLooseEvent` 会把它们整发替换成一支普通箭）。两条边界：

- **只认普通箭**：模组弓自己 `new EntityArrow`，从箭筒拿药水箭会丢掉药水效果，所以药水箭
  仍然只给原版弓用；箭筒里只有药水箭时模组弓不能开火。
- **背包优先**：背包里真有普通箭时先扣背包的，箭筒只在背包没有时被扣。

因为改的是原版类，这个 jar 现在**需要 UniMixins**（GTNH 整合包自带）。像 RiftFlux 的
World Explorer 长弓那种自己扫主背包、再找自家箭筒的弓不在适配范围内。
