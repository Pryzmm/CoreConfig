# What is CoreConfig?
CoreConfig is a modern and easy-to-use configuration mod that allows developers to easily implement settings that users can change at will. CoreConfig is highly customizable and can even have decorated banners, button colors and more, specific to the viewed mod, making it a viable option for new mods.

<img width="960" height="540" alt="image" src="https://github.com/user-attachments/assets/f935da62-a88f-4ae4-bda4-92a88e7faf51" />

# Features
CoreConfig comes with a bunch of features for developers:
- Configuration support for Strings/Integers/Floats/Doubles/Colors/Booleans
- Website buttons to open a site in a user's browser
- Custom buttons that can run any Runnable
- Mod-specific customization (image banner, button colors, and more!)
- Configuration validation
- Sorting and option dividers
- ...and more!

It also has some perks for players using the CoreConfig mod:
- Modify all supported mods in one menu
- Extremely simplistic for ease of use with a user-friendly interface

# Implementation
To use this in your own mod, you can add Jitpack as a repository, then implement my API directly into the mod. This makes having the configuration mod *optional* and players will not have to download CoreConfig alongside your mod. Below is how to implement it with gradle or maven.

> [!NOTE]
> If players do not have the CoreConfig mod installed, supplies default values will **ALWAYS** be used.

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation "com.github.Pryzmm.CoreConfig:coreconfig-api:7a09fde"
    // 7a09fde is the commit, and may change. Make sure to check the GitHub's main page for the latest API version!
}
```

***

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Pryzmm.CoreConfig</groupId>
        <artifactId>coreconfig-api</artifactId>
        <version>7a09fde</version>
    </dependency>
</dependencies>
```

Once implemented, you need to register your mod into the API. Luckily, this only requires one line! Wherever your mod starts to initialize, you can use this method:
```java
// Simple Registration (Just gets the mod into the API)
ConfigRegistrar.register(Identifier modNameIdentifier);
// Full Registration (For more customization)
ConfigRegistrar.register(Identifier modNameIdentifier, @Nullable Identifier modBannerIdentifier, @Nullable Integer backgroundColor, @Nullable Integer buttonColor);

// modNameIdentifier: Not Null; serves as a way to register your mod but also allows to translate your mod name into the sidebar.
// modBannerIdentifier: Nullable; allows a texture to be used as a background for your mod in the sidebar. (Scaled to 200x30)
// backgroundColor: Nullable; an ARGB color used for the right-side containers of your mod
// buttonColor: Nullable; an ARGB color used for the right-side buttons of your mod
```

Your mod icon is automatically used if it's found in `assets/<MODID>/icon.png` or `assets/<MODID>/textures/icon.png`, otherwise, a default missing texture will be used.

# Support
You can support the development of this mod and future releases by becoming a sponsor on GitHub or supporting my Ko-Fi!

<a href="https://ko-fi.com/Pryzmm"><img alt="github-sponsor" src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/donate/ghsponsors-singular_64h.png"></a>
<a href="https://github.com/sponsors/Pryzmm"><img alt="kofi-sponsor" src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/donate/kofi-singular-alt_64h.png"></a>
