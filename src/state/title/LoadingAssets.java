package state.title;

import state.LoadingState;

public final class LoadingAssets {
	public enum Route {
		TITLE,
		PLAYING
	}

	private LoadingAssets() {}

	public static void preload(LoadingState loading, Route route) {
		loading.preloadImage("CircleBullet");

		switch (route) {
			case TITLE -> preloadTitle(loading);
			case PLAYING -> preloadPlaying(loading);
		}
	}

	private static void preloadTitle(LoadingState loading) {
		loading.preloadImage("TSC1");
		loading.preloadImage("TSC2F");
		loading.preloadImage("TSC3F");
		loading.preloadImage("TSC4");
		loading.preloadImage("TSC5");
	}

	private static void preloadPlaying(LoadingState loading) {
		loading.preloadImage("OvalBullet");
		loading.preloadImage("LAMBDASCT");
		loading.preloadImage("ReimuSCT");
		loading.preloadSound("[TH] Graze");
		loading.preloadSound("[TH] Fires");
	}
}
