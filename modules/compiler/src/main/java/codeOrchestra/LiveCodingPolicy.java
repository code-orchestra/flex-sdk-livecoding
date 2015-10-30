package codeOrchestra;

/**
 * @author Alexander Eliseyev
 */
public enum LiveCodingPolicy {

    DISABLED,
    LIVE_CLASS,
    SELECTED_METHODS;

    public boolean isEnabled() {
        return this == LIVE_CLASS || this == SELECTED_METHODS;
    }

}
