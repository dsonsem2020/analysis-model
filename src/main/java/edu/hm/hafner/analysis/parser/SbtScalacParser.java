package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the sbt scala compiler warnings. You should use -feature and -deprecation compiler opts.
 *
 * @author Hochak Hung
 */
public class SbtScalacParser extends RegexpLineParser {
    private static final long serialVersionUID = -4233964844965517977L;

    private static final String SBT_WARNING_PATTERN = "^(\\[warn\\]|\\[error\\](?!\\s+Total\\stime:))\\s*(.*?):(\\d+)(?::\\d+)?:\\s*(.*)$";



    /**
     * Creates a new instance of {@link SbtScalacParser}.
     */
    public SbtScalacParser() {
        super(SBT_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(2))
                .setLineStart(matcher.group(3))
                .setMessage(matcher.group(4))
                .setSeverity(mapPriority(matcher))
                .buildOptional();
    }

    private Severity mapPriority(final Matcher matcher) {
        if ("[error]".equals(matcher.group(1))) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_NORMAL;
        }
    }
}
