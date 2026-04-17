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
> If players do not have the CoreConfig mod installed, supplied default values will **ALWAYS** be used.

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation "com.github.pryzmm:CoreConfig:9a7ff3661a"
    // 9a7ff3661a is the commit, and may change. Make sure to check the GitHub's main page for the latest API version!
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
        <groupId>com.github.pryzmm</groupId>
        <artifactId>CoreConfig</artifactId>
        <version>9a7ff3661a</version>
    </dependency>
</dependencies>
```

# Support
You can support the development of this mod and future releases by becoming a sponsor on GitHub or supporting my Ko-Fi!

<a href="https://github.com/sponsors/Pryzmm"><img alt="github-sponsor" src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/donate/ghsponsors-singular_64h.png"></a>
<a href="https://ko-fi.com/Pryzmm"><img alt="kofi-sponsor" src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/donate/kofi-singular-alt_64h.png"></a>
