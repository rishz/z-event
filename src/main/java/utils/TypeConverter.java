package utils;

/**
 * Defines an interface for converting values from and to their physical
 * materializations and their virtual representations.
 *
 * @param <P>
 *            the physical type of value
 * @param <V>
 *            the virtual type of value
 */
public interface TypeConverter<P, V> {

    /**
     * Converts a virtual representation of a value into it's physical value.
     *
     * @param virtualValue
     *            the virtual representation
     * @return the physical value
     */
    public P toPhysicalValue(V virtualValue);

    /**
     * Converts a physical value into it's virtual representation.
     *
     * @param physicalValue
     *            the physical value
     * @return the virtual representation
     */
    public V toVirtualValue(P physicalValue);
}