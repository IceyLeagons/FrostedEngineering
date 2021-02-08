# FrostedEngineering

Frosted Engineering is a Minecraft plugin framework designed to create Minecraft mod like experience in Spigot/Bukkit/Paper.

We achieve this with in-plugin resource pack generation to supply players with a collection of custom models and sounds you may register in the API.
We also inject things directly into the Minecraft server, so we can have custom biomes, entities and so on!

So to say it's a more modern version of SlimeFun, but just a framework you can use, so it's somewhat similiar to Fabric.

## Getting Started

This category will explain how to get started with FrostedEngineering, as a developer or as a server owner.

### As a Server Owner

To install the plugin, simply download the jar from our Spigot page (or from the releases here), and put it inside your plugins folder.

If you decide to go with the unstable route, you can get access to updates, even if they've not been released just yet.
You'll need to build the project yourself, but sometimes we do publish some pre-releases/snapshots.

After installation you shall download the addons of your liking, and configure them.
You may also want to change some settings in FrostedEngineering as well.

### As a Developer

To keep this read me clean, we've decided to move this to a section in our Wiki.

## Main Concept/Structure

FrostedEngineering cannot do much on its own, but it is required, since it's our core. It contains all the default implementations for our API.

In FrostedEngineering we refer to every "mod/plugin" as addons. These addons work just like the plugin system you're using with any sort of Spigot/Bukkit server; as a developer
you can still access every Bukkit method through FrostedEngineering. We did this so everything(enabling, disabling, registering, etc.) is managable from the core
and our custom ecosystem. In this ecosystem the addons can also communicate with each other very easily.
