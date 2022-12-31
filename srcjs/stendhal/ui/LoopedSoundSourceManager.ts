/***************************************************************************
 *                    Copyright © 2003-2022 - Stendhal                     *
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License as        *
 *   published by the Free Software Foundation; either version 3 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 ***************************************************************************/

import { Sound } from "./SoundManager";
import { SoundManager } from "./SoundManager";

import { LoopedSoundSource } from "../entity/LoopedSoundSource";

declare var marauroa: any;
declare var stendhal: any;


export class LoopedSoundSourceManager {

	private readonly sndMan = SoundManager.get();
	private static instance?: LoopedSoundSourceManager;

	private sources: {[id: string]: Sound} = {};


	/**
	 * Retrieves singleton instance.
	 *
	 * @return
	 *     LoopedSoundSourceManager.
	 */
	static get(): LoopedSoundSourceManager {
		if (!LoopedSoundSourceManager.instance) {
			LoopedSoundSourceManager.instance = new LoopedSoundSourceManager();
		}
		return LoopedSoundSourceManager.instance;
	}

	private constructor() {
		// do nothing
	}

	/**
	 * Retrieves the looped sound sources for the current zone.
	 *
	 * @return
	 *     Sound sources.
	 */
	getSources(): {[id: string]: Sound} {
		return this.sources;
	}

	/**
	 * Adds a new looped sound source to be played.
	 *
	 * @param source
	 *     Sound source.
	 * @return
	 *     <code>true</code> if addition succeeded.
	 */
	addSource(source: LoopedSoundSource): boolean {
		const id = source["id"];
		if (!marauroa.me) {
			console.warn("tried to add looped sound source with ID '" + id + "' before player was ready");
			return false;
		}
		if (this.sources[id]) {
			console.warn("tried to add looped sound source with existing ID '" + id + "'");
			return true;
		}

		const snd = this.sndMan.playLocalizedMusic(source["x"], source["y"],
				source["radius"], source["layer"], source["sound"],
				source["volume"]);

		if (!snd) {
			console.error("failed to add looped sound source with ID '" + id + "'");
			return false;
		}

		this.sources[id] = snd;
		return true;
	}

	/**
	 * Removes a currently playing sound source.
	 *
	 * @param id
	 *     Sound source identifier.
	 * @return
	 *     <code>true</code> if removal succeeded.
	 */
	removeSource(id: string): boolean {
		const source = this.sources[id];
		if (typeof(source) === "undefined") {
			console.warn("tried to remove unknown looped sound source with ID '" + id + "'");
			return true;
		}

		delete this.sources[id];

		const errmsg = [];
		if (!this.sndMan.stopLoop(source)) {
			errmsg.push("failed to stop looped sound source with ID '" + id + "'");
		}
		if (this.sources[id]) {
			errmsg.push("failed to remove looped sound source with ID '" + id + "'");
		}

		if (errmsg.length > 0) {
			for (const msg of errmsg) {
				console.error(msg);
			}
			return false;
		}
		return true;
	}

	/**
	 * Retrieves a list of looped sound sources.
	 *
	 * @return
	 *     All <code>LoopedSoundSource</code> entities in current zone.
	 */
	private getZoneEntities(): LoopedSoundSource[] {
		const ents: LoopedSoundSource[] = [];
		if (stendhal.zone.entities) {
			for (const ent of stendhal.zone.entities) {
				if (ent instanceof LoopedSoundSource) {
					ents.push(ent);
				}
			}
		}

		return ents;
	}

	/**
	 * This is called after zone is created to make sure looped sound
	 * sources are added properly.
	 */
	onZoneReady() {
		for (const ent of this.getZoneEntities()) {
			if (!ent.isLoaded()) {
				this.addSource(ent);
			}
		}
	}

	/**
	 * Adjusts volume level for each looped sound source in current zone.
	 *
	 * @param x
	 *     The new X coordinate of listening entity.
	 * @param y
	 *     The new Y coordinate of listening entity.
	 */
	onDistanceChanged(x: number, y: number) {
		for (const ent of this.getZoneEntities()) {
			if (ent.isLoaded()) {
				this.sndMan.adjustForDistance(this.sources[ent["id"]],
						ent["radius"], ent["x"], ent["y"], x, y);
			}
		}
	}
}