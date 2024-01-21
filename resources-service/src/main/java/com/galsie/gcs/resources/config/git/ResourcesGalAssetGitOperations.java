package com.galsie.gcs.resources.config.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
@Slf4j
public class ResourcesGalAssetGitOperations implements InitializingBean{


    @Value("${galsie.resource.galassets.git.localRepositoryLocation}")
    private String localRepoPath;

    @Value("${galsie.resource.galassets.git.remoteRepositoryURI}")
    private String remoteRepoURI;

    @Value("${galsie.resource.galassets.git.branchName}")
    private String branchName;

    @Value("${galsie.resource.galassets.git.pat}")
    private String pat;

    @Value("${galsie.resource.galassets.git.username}")
    private String username;

    private Git git;

    private UsernamePasswordCredentialsProvider gitCredentialsProvider;

    @Override
    public void afterPropertiesSet() throws GitAPIException, IOException {
        var localRepoDirectory = new File(localRepoPath);
        gitCredentialsProvider = new UsernamePasswordCredentialsProvider(username, pat);
        if(localRepoDirectory.exists() && localRepoDirectory.isDirectory() && localRepoDirectory.list().length > 0){
            log.info("Local repo already exists, skipping cloning");
            git = Git.open(localRepoDirectory);
            return;
        }
        Path repoPath = Paths.get(localRepoPath);
        Files.createDirectories(repoPath);
        git = Git.cloneRepository()
                .setURI(remoteRepoURI)
                .setDirectory(new File(localRepoPath))
                .setBranch(branchName)
                .setCredentialsProvider(gitCredentialsProvider)
                .call();
        log.info("Created local repo directory");
    }


    public void pullChanges() {
        try {
            git.pull().setCredentialsProvider(gitCredentialsProvider).call();
        } catch (GitAPIException e) {
            log.info("Failed to pull changes from remote repo ", e);
        }
    }

    public Optional<String> checkLocalRepoLastCommitSha() {
        try(var git  = Git.open(new File(localRepoPath))) {
            var localLastCommitSHA = git.getRepository().resolve("refs/heads/" + branchName).getName();
            log.info("Local last commit SHA: " + localLastCommitSHA);
            return Optional.of(localLastCommitSHA);
        } catch (IOException e) {
            log.error("Failed to check local repo last commit SHA", e);
            return Optional.empty();
        }
    }

    public Optional<String> checkOnlineRepoLastCommitSHA() {
        try{
            var lastOnlineCommitSHA = git.getRepository().resolve("refs/remotes/origin/" + branchName).getName();
            log.info("Online last commit SHA: " + lastOnlineCommitSHA);
            return Optional.of(lastOnlineCommitSHA);
        } catch (Exception e) {
            log.error("Failed to check online repo last commit SHA", e);
            return Optional.empty();
        }
    }
}
