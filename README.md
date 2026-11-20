# Head TPA Here (Fabric, Minecraft 1.21.11)

Click a player's head in a menu (e.g. DonutSMP's /tpa player list) and the mod
sends `/tpahere <thatPlayer>` for you. Client-side only, needs Fabric Loader (no Fabric API).

## Build
1. Install JDK 21.
2. This zip has no Gradle wrapper (it's a binary file). Get one by either:
   - copying `gradlew`, `gradlew.bat` and the `gradle/` folder from
     https://github.com/FabricMC/fabric-example-mod (branch `1.21.11`), or
   - running `gradle wrapper` if you have Gradle installed.
3. Run `./gradlew build` (Windows: `gradlew build`).
4. Your mod is `build/libs/headtpahere-1.0.0.jar` (not the `-sources` one).
5. Drop it in your `.minecraft/mods` folder with Fabric Loader 0.18+ for 1.21.11.

## Tweaks (src/main/java/com/headtpahere/HeadTpaHere.java)
- `COMMAND` - `tpahere` by default
- `CANCEL_ORIGINAL_CLICK` - set false if you still want the menu's own click to go through
- `COOLDOWN_MS` - anti double-click delay

Heads in your own inventory are ignored, and so is your own head.

## Build with GitHub (no Java/Gradle install needed)
1. Create a new repo on github.com and upload everything in this folder
   (build.gradle must sit at the top level of the repo).
2. Make sure `.github/workflows/build.yml` made it up.
3. Open the repo's **Actions** tab, wait for the green check.
4. Open that run, scroll to **Artifacts**, download `headtpahere`, unzip it,
   and put the jar in your `mods` folder.
Every time you edit a file and commit, GitHub rebuilds automatically.
