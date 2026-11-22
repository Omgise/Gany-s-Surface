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

While a quiver is worn it is drawn on the player's back, from the item's own icon,
so the empty art and the with-arrows art (texture packs such as Modernity draw the
latter at `minecraft:quiver`) always match the inventory. The sprite leans with the
torso pose, which carries it through vanilla sneaking and EvenMoBends' animated
body, and the cape is hidden while the quiver occupies that spot. Its placement is
a constant in `QuiverPlayerRenderer` (`SCALE` / `OFFSET_X` / `OFFSET_Y` / `OFFSET_Z`).

### Modded bows

Other mods' bows never call this mod. They ask `InventoryPlayer` instead:
`hasItem(Items.arrow)` decides whether they may draw and fire, and
`consumeInventoryItem(Items.arrow)` spends an arrow. `MixinInventoryPlayerQuiver`
answers both questions from the quiver, so a bow keeps its own arrow entity and
the quiver simply acts as its ammo:

| Bow | Result |
|---|---|
| vanilla bow | unchanged: it fires plain or tipped quiver arrows through the `ArrowLooseEvent` handler |
| Twilight Forest (triple / ice / seeker / ender) | plain quiver arrows: its own arrow, so the triple shot, homing, freezing and ender teleport survive. A tipped quiver arrow: one tipped arrow, like a tipped arrow in the inventory |
| Natura (fusewood), CoFH bows (e.g. Thermal Foundation's Invar Bow), RiftFlux's More Bows / Searing Bow, LegendGear | draw and fire with nothing but a quiver on the belt |
| Natura (plain), Ender Zoo's Guardians Bow, Mekanism's electric bow | draw and fire plain quiver arrows; they never post `ArrowLooseEvent`, so tipped arrows from the quiver do not apply |
| Draconic Evolution | spends the quiver's plain arrows for its custom arrows; its own ender arrows are untouched |
| Avaritia's Infinity Bow | untouched (it does not consume arrows) |

Three rules are deliberate:

* **a plain quiver arrow keeps the bow's own shot** — a modded bow spawns its own
  `EntityArrow` (triple shot, ice, homing, ...), so plain arrows are simply ammo for it.
* **a tipped quiver arrow is fired as Et Futurum's tipped arrow, for any bow that
  posts `ArrowLooseEvent`** — that is exactly what Et Futurum already does for tipped
  arrows lying in the inventory, so the shot becomes one tipped arrow instead of the
  bow's special arrow. Bows that never post the event (Ender Zoo, Mekanism) only get
  plain arrows.
* **the inventory wins** — a real plain arrow stack in the inventory is spent first,
  and a tipped arrow in the inventory is left to Et Futurum's own handler; the quiver
  is only used when the inventory has neither.

### Weapon Mod ammo

Balkon's Weapon Mod does not ask `InventoryPlayer` either: its ranged weapons scan
the inventory through `WMItem.findAnyItemSlot` and spend ammo from `mainInventory`
directly, so the mixin above cannot reach them. The quiver gets a second,
**late-loaded** mixin for it (`MixinWeaponModRangedComponent`, registered from
`LateMixins` and only when `weaponmod` is installed) that answers the two
`RangedComponent` ammo questions when the inventory has nothing:

* `hasAmmo` — the heavy crossbow (and the musket, blunderbuss, flintlock, mortar and
  blowgun) accepts quiver ammo, so right-click starts the reload;
* `hasAmmoAndConsume` — the released shot spends one round from the quiver.

What counts as ammo is read from the crossbow itself
(`RangedComponent.getAmmoItems()`), so it is exactly **crossbow bolts** — the mod's other
ranged weapons (musket, blunderbuss, flintlock, mortar, blowgun) are firearms and keep
their own cartridges out of the quiver. Bolts go into their own NBT list, leave the arrow
counter alone, share the quiver's capacity with the arrows, and appear in the tooltip and
in the sneak-click dump like everything else.

Bows that keep a private ammo model of their own (RiftFlux's World Explorer Long
Bow, which reads the main inventory directly and then its own quiver) are still not
routed through the quiver.

Patching a vanilla class means the mod ships a mixin config and needs
**UniMixins** at runtime (every GTNH instance has it). `usesMixins = true` in
`gradle.properties` makes the GTNH convention add the manifest entries, the
UniMixins requirement and the refmap; the targets live in `MixinInventoryPlayerQuiver`
(main config) and `MixinWeaponModRangedComponent` (late config, other mod).

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
接到箭筒上，所以暮色森林的四把弓、Natura、Ender Zoo、CoFH（热力基础的弓）、Mekanism 电动弓、
RiftFlux 的 More Bows / 灼热弓、LegendGear 都能只靠腰带上的箭筒拉弓放箭。三条边界：

- **普通箭**：模组弓自己 `new EntityArrow`，所以普通箭只是它的弹药，三重弓/冰弓/追踪弓的
  特殊箭矢照常生效（原先 `ArrowLooseEvent` 会把它们整发替换成一支普通箭）。
- **药水箭**：箭筒里的药水箭按 Et Futurum 的 `EntityTippedArrow` 打出去（与放在背包里时的
  行为一致），对所有会发 `ArrowLooseEvent` 的弓生效——原版弓、暮色四把、CoFH、Natura 的
  Fusewood、RiftFlux、DE、LegendGear。不发这个事件的弓（Ender Zoo、Mekanism 电动弓）只用普通箭。
- **背包优先**：背包里真有普通箭时先扣背包的；背包里有药水箭时交给 Et Futurum 自己的处理器，
  箭筒只在背包都没有时才被扣。

因为改的是原版类，这个 jar 现在**需要 UniMixins**（GTNH 整合包自带）。

**箭筒装弩箭（Balkon's Weapon Mod / 更多武器）**：它的远程武器不走 `InventoryPlayer`，而是
`WMItem.findAnyItemSlot` 扫背包、直接从 `mainInventory` 扣弹药，所以上面那个 mixin 够不着。
新增**晚加载**的 `MixinWeaponModRangedComponent`（`LateMixins` 注册，只在装了 `weaponmod`
时生效）：`hasAmmo` 让重弩把箭筒当成弹药来源、右键能上膛；`hasAmmoAndConsume` 松手时从箭筒
扣一发。算弹药的物品直接问重弩自己（`RangedComponent.getAmmoItems()`），所以**只收弩箭**——
那个模组的火枪、散弹枪、燧发枪、迫击炮、吹箭筒属于枪械/吹箭，不占箭筒。弩箭单独存在 `Ammo`
里，不碰箭矢计数，和箭共用箭筒容量，提示框和潜行右键倒出都会列出来。

像 RiftFlux 的 World Explorer 长弓那种自己扫主背包、再找自家箭筒的弓仍然不在适配范围内。
