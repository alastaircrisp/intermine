package org.intermine.webservice.server.jbrowse;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents an interbase segment.
 */
public final class Segment {

    public final String section;
    public final Integer start, end;

    private Segment(String section, Integer start, Integer end) {
        this.section = section;
        this.start = start;
        this.end = end;
    }

    public final static Segment NEGATIVE_SEGMENT = new Segment(null, null, null);
    public final static Segment GLOBAL_SEGMENT = new Segment(null, null, null);

    public static Segment makeSegment(String ref, Integer s, Integer e) {
        if (("global").equals(ref)) {
            return GLOBAL_SEGMENT;
        }
        if (s != null && e != null && s < 0 && e < 0) {
            return NEGATIVE_SEGMENT; // Represents all out of band segments
        }
        return new Segment(ref, ((s == null) ? null : Math.max(0, s)), e);
    }

    public String getSection() {
        return this.section;
    }

    public Integer getStart() {
        return this.start;
    }

    public Integer getEnd() {
        return this.end;
    }

    public Integer getWidth() {
        if (this.end == null || this.start == null) {
            return null;
        }
        return this.end - this.start;
    }

    public String toRangeString() {
        if (start == null && end == null) {
            return section;
        } else if (start == null || end == null) {
            throw new RuntimeException("Not implemented"); // TODO
        } else {
            return String.format("%s:%d..%d", section, start + 1, end + 1);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(section).append(start).append(end).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    public Segment subsegment(int i, int j) {
        if (start != null && i < start)
            throw new IllegalArgumentException("i is less than start");
        if (end != null && j > end)
            throw new IllegalArgumentException(
                    String.format("j (%d) is greater than end (%d)", j, end));
        return new Segment(section, i, j);
    }
}
