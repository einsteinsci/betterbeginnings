Better Beginnings Mod
=====================

[![Join the chat at https://gitter.im/einsteinsci/betterbeginnings](https://badges.gitter.im/einsteinsci/betterbeginnings.svg)](https://gitter.im/einsteinsci/betterbeginnings?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Better Beginnings mod for Minecraft v1.8.x. Requires Forge (built with 11.14.4.1577).

Kind of complicated. See the [forum topic](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/wip-mods/2192122-betterbeginnings-mod).

Source for 1.7.10 version:
--------------------------
Since IntelliJ doesn't exactly play nice with GitHub branches, I had to move the MC1.7 branch to [its own repository](https://github.com/einsteinsci/betterbeginnings-MC1.7). You can find it here. Issues for the 1.7.10 version should still be posted here.

Contributing to this project:
-----------------------------
1. Download Forge v11.14.4.1577
2. Setup with gradlew setupDecompWorkspace and gradlew eclipse
3. Fork and clone this repository in a separate folder
4. Add a new project, with src/main/java and src/main/resources
5. Change the src/main/java and src/main/resources folder to use the src/main/java and src/main/resources folders from the source you downloaded.
6. Delete the src/main/java and src/main/resources from the default "minecraft" package
7. Add the project to the build path (or something like that)
8. Do cool stuff with the code
9. When you're finished with your changes, submit a pull request.

License
-------
This software is licensed under a [GPL v3 License](http://www.gnu.org/copyleft/gpl.html). A copy is provided in this repository in License.txt.
