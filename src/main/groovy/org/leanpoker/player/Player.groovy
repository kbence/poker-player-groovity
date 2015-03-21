package org.leanpoker.player

class Player {

    static final String VERSION = 'Default Groovy folding player';

    static int betRequest(def gameState) {
        print gameState
        Math.floor(Math.random() * 1000) as Integer
    }

    static void showdown(def gameState) {
    }
}
