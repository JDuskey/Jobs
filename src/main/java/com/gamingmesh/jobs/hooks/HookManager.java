package com.gamingmesh.jobs.hooks;

import org.bukkit.plugin.PluginManager;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.hooks.McMMO.McMMO1_X_listener;
import com.gamingmesh.jobs.hooks.McMMO.McMMO2_X_listener;
import com.gamingmesh.jobs.hooks.McMMO.McMMOManager;
import com.gamingmesh.jobs.hooks.MyPet.MyPetManager;
import com.gamingmesh.jobs.hooks.MythicMobs.MythicMobInterface;
import com.gamingmesh.jobs.hooks.MythicMobs.MythicMobs4;
import com.gamingmesh.jobs.hooks.WorldGuard.WorldGuardManager;
import com.gamingmesh.jobs.hooks.stackMob.StackMobHandler;
import com.gamingmesh.jobs.hooks.wildStacker.WildStackerHandler;

public class HookManager {

    private static McMMOManager McMMOManager;
    private static MythicMobInterface MythicManager;
    private static MyPetManager myPetManager;
    private static WorldGuardManager worldGuardManager;
    private static StackMobHandler stackMobHandler;
    private static WildStackerHandler wildStackerHandler;

    private static PluginManager pm;

    public static void loadHooks() {
	pm = Jobs.getInstance().getServer().getPluginManager();

	if (getMcMMOManager().CheckmcMMO())
	    setMcMMOlistener();

	setMyPetManager();
	setWorldGuard();
	setMythicManager();
	setStackMobHandler();
	setWildStackerHandler();

	if (checkMythicMobs())
	    MythicManager.registerListener();
    }

    public static StackMobHandler getStackMobHandler() {
	if (stackMobHandler == null) {
	    setStackMobHandler();
	}

	return stackMobHandler;
    }

    public static WildStackerHandler getWildStackerHandler() {
	if (wildStackerHandler == null) {
	    setWildStackerHandler();
	}

	return wildStackerHandler;
    }

	public static MyPetManager getMyPetManager() {
	if (myPetManager == null) {
	    setMyPetManager();
	}

	return myPetManager;
    }

    public static WorldGuardManager getWorldGuardManager() {
	if (worldGuardManager == null) {
	    worldGuardManager = new WorldGuardManager();
	}

	return worldGuardManager;
    }

    public static McMMOManager getMcMMOManager() {
	if (McMMOManager == null)
	    McMMOManager = new McMMOManager();

	return McMMOManager;
    }

	public static MythicMobInterface getMythicManager() {
	return MythicManager;
    }

    public static boolean checkMythicMobs() {
	return Jobs.getGCManager().MythicMobsEnabled && MythicManager != null && MythicManager.Check();
    }

    private static boolean setWorldGuard() {
	if (isPluginEnabled("WorldGuard")) {
	    worldGuardManager = new WorldGuardManager();
	    Jobs.consoleMsg("&e[Jobs] WorldGuard detected.");
	    return true;
	}

	return false;
    }

    private static void setMythicManager() {
	if (!isPluginEnabled("MythicMobs"))
	    return;

	try {
	    Class.forName("io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper");
	    MythicManager = new MythicMobs4(Jobs.getInstance());
	} catch (ClassNotFoundException ex) {
	}

	if (MythicManager == null) {
	    Jobs.consoleMsg("&cYour MythicMobs version is not supported by Jobs! Supported versions: 4.9.1+");
	    return;
	}

	Jobs.consoleMsg("&e[Jobs] MythicMobs detected.");
    }

    public static void setMcMMOlistener() {
	Jobs ins = Jobs.getInstance();
	try {
	    Class.forName("com.gmail.nossr50.datatypes.skills.SuperAbilityType");
	    pm.registerEvents(new McMMO2_X_listener(ins), ins);
	    Jobs.consoleMsg("&e[Jobs] Registered McMMO 2.x listener");
	} catch (ClassNotFoundException e) {
	    pm.registerEvents(new McMMO1_X_listener(ins), ins);
	    Jobs.consoleMsg("&e[Jobs] Registered McMMO 1.x listener");
	}
    }

    private static void setMyPetManager() {
	if (isPluginEnabled("MyPet")) {
	    myPetManager = new MyPetManager();
	    Jobs.consoleMsg("&e[Jobs] MyPet detected.");
	}
    }

    private static void setStackMobHandler() {
	if (isPluginEnabled("StackMob")) {
	    stackMobHandler = new StackMobHandler();
	}
    }

    private static void setWildStackerHandler() {
	if (isPluginEnabled("WildStacker")) {
	    wildStackerHandler = new WildStackerHandler();
	}
    }

    public static boolean isPluginEnabled(String name) {
	return pm.getPlugin(name) != null && pm.isPluginEnabled(name);
    }
}
