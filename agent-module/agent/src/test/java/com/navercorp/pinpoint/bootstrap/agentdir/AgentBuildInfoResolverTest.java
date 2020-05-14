package com.navercorp.pinpoint.bootstrap.agentdir;

import com.navercorp.pinpoint.common.Version;
import com.navercorp.pinpoint.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class AgentBuildInfoResolverTest {

    private static final String BOOTSTRAP_JAR = "pinpoint-bootstrap-" + Version.VERSION + ".jar";

    @Test
    public void testResolve() {
        AgentBuildInfoResolver resolver = getAgentBuildInfoResolver();
        AgentBuildInfo buildInfo = resolver.resolve();
        Assertions.assertTrue(buildInfo.isAvailable(), "info available");
        Assertions.assertTrue(StringUtils.hasText(buildInfo.getGitCommitIdAbbrev()), "info commit id abbrev");
        Assertions.assertTrue(StringUtils.hasText(buildInfo.getGitBranch()), "info branch");
    }

    private static AgentBuildInfoResolver getAgentBuildInfoResolver() {
        final String testClassPath = AgentBuildInfoResolverTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        final Path testClassDir = new File(testClassPath).toPath();
        final Path agentDir = testClassDir.resolveSibling("pinpoint-agent-" + Version.VERSION);
        Path agentJarFullPath = agentDir.resolve(BOOTSTRAP_JAR);
        BootDir bootDir = new BootDir(agentDir.resolve("boot"), Collections.emptyList());
        List<Path> libs = Collections.emptyList();
        List<Path> plugins = Collections.emptyList();
        Path agentJarPath = new File(BOOTSTRAP_JAR).toPath();
        final AgentDirectory agentDirectory = new AgentDirectory(agentJarPath, agentJarFullPath, agentDir, bootDir, libs, plugins);
        return new AgentBuildInfoResolver(agentDirectory);
    }
}
