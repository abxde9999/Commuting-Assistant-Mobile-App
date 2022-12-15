package com.example.biyahe.model;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

/**
 * Location types for a reverse geocoding request. Please see <a
 * href="https://developers.google.com/maps/documentation/geocoding/start#reverse">Reverse
 * Geocoding</a> for more detail.
 */
public enum LocationType {
    /**
     * Restricts the results to addresses for which we have location information accurate down to
     * street address precision.
     */
    ROOFTOP,

    /**
     * Restricts the results to those that reflect an approximation (usually on a road) interpolated
     * between two precise points (such as intersections). An interpolated range generally indicates
     * that rooftop geocodes are unavailable for a street address.
     */
    RANGE_INTERPOLATED,

    /**
     * Restricts the results to geometric centers of a location such as a polyline (for example, a
     * street) or polygon (region).
     */
    GEOMETRIC_CENTER,

    /** Restricts the results to those that are characterized as approximate. */
    APPROXIMATE,

    /**
     * Indicates an unknown location type returned by the server. The Java Client for Google Maps
     * Services should be updated to support the new value.
     */
    UNKNOWN;
}

