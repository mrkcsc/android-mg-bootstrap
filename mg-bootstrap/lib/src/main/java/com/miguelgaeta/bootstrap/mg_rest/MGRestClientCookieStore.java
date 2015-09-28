package com.miguelgaeta.bootstrap.mg_rest;

import android.support.annotation.NonNull;

import com.miguelgaeta.bootstrap.mg_preference.MGPreference;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * Cookie store that also persists cookies and
 * restores them upon instance creation.
 */
class MGRestClientCookieStore implements CookieStore {

    @Getter(lazy = true)
    private static final MGPreference<Map<String, HttpCookie>> persistentCookies = MGPreference.create("COOKIE_STORE", new HashMap<>());

    @Getter(lazy = true)
    private final CookieStore cookieStore = new CookieManager().getCookieStore();

    /**
     * When initialize, read from persistent cookies
     * and restore any cookies written to disc.
     *
     * Note: Persisted cookies are shared across instances.
     */
    public MGRestClientCookieStore() {

        for (HttpCookie cookie : getPersistentCookies().get().values()) {

            getCookieStore().add(URI.create(cookie.getDomain()), cookie);
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {

        addPersistentCookie(cookie);

        getCookieStore().add(URI.create(cookie.getDomain()), cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {

        return getCookieStore().get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {

        return getCookieStore().getCookies();
    }

    @Override
    public List<URI> getURIs() {

        return getCookieStore().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {

        removePersistentCookie(cookie);

        return getCookieStore().remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {

        removeAllPersistentCookies();

        return getCookieStore().removeAll();
    }

    /**
     * Adds a cookie to persisted map.
     */
    private void addPersistentCookie(@NonNull HttpCookie cookie) {

        Map<String, HttpCookie> cookies = getPersistentCookies().get();

        cookies.put(cookie.getDomain() + "|" + cookie.getName(), cookie);

        getPersistentCookies().set(cookies);
    }

    /**
     * Removes a cookie from persisted map.
     */
    private void removePersistentCookie(@NonNull HttpCookie cookie) {

        Map<String, HttpCookie> cookies = getPersistentCookies().get();

        cookies.remove(cookie.getDomain() + "|" + cookie.getName());

        getPersistentCookies().set(cookies);
    }

    /**
     * Remove all cookies from persisted map.
     */
    private void removeAllPersistentCookies() {

        getPersistentCookies().clear();
    }
}