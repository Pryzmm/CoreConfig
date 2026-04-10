package com.pryzmm.coreconfigapi.data;

/**
 * {@link ConfigType} allows for synchronization of client and server configurations.
 * <p>
 * <code>CLIENT</code> Configuration is only stored on the client and is not synchronized to the server. This is ideal for client-side settings that do not affect gameplay.
 * <p>
 * <code>SERVER</code> Configuration is stored on the server and is synchronized to clients when they connect. This is ideal for server-side settings such as gameplay mechanics, world generation options, or other settings that affect gameplay.
 * <p>
 * <code>BOTH</code> Configuration is stored on both the client and server and is synchronized between them. This can be changed on the clients end, but server settings will take priority over client settings. This is ideal for settings that can be customized by the player but should be overridden by the server for consistency.
*/
public enum ConfigType { CLIENT, SERVER, BOTH }