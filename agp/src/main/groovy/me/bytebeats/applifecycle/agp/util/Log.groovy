package me.bytebeats.applifecycle.agp.util

import org.gradle.api.logging.Logger

/**
 * Created by bytebeats on 2021/12/15 : 11:24
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */

class Log {
    static Logger mLogger

    static void init(Logger logger) {
        mLogger = logger
    }

    static void quiet(CharSequence message) {
        makeSureLoggerInitialized()
        mLogger.quiet(message)
    }


    static void quiet(CharSequence message, Object... objects) {
        makeSureLoggerInitialized()
        mLogger.quiet(message, objects)
    }

    static void quiet(CharSequence message, Throwable throwable) {
        makeSureLoggerInitialized()
        mLogger.quiet(message, throwable)
    }

    static void info(CharSequence message) {
        makeSureLoggerInitialized()
        mLogger.info(message)
    }

    static void info(CharSequence message, Object... objects) {
        makeSureLoggerInitialized()
        mLogger.info(message, objects)
    }

    static void info(CharSequence message, Throwable throwable) {
        makeSureLoggerInitialized()
        mLogger.info(message, throwable)
    }

    static void debug(CharSequence message) {
        makeSureLoggerInitialized()
        mLogger.debug(message)
    }


    static void debug(CharSequence message, Object... objects) {
        makeSureLoggerInitialized()
        mLogger.debug(message, objects)
    }


    static void debug(CharSequence message, Throwable throwable) {
        makeSureLoggerInitialized()
        mLogger.debug(message, throwable)
    }

    static void error(CharSequence message) {
        makeSureLoggerInitialized()
        mLogger.error(message)
    }

    static void error(CharSequence message, Object... objects) {
        makeSureLoggerInitialized()
        mLogger.error(message, objects)
    }

    static void error(CharSequence message, Throwable throwable) {
        makeSureLoggerInitialized()
        mLogger.error(message, throwable)
    }

    private static void makeSureLoggerInitialized() {
        if (mLogger == null) {
            throw IllegalStateException("Log#init(Logger) is not initialized")
        }
    }
}