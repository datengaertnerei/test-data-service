const { Octokit } = require("@octokit/rest");

const repo = "test-data-service";
const owner = "datengaertnerei";
const state = "open";

const github = new Octokit({
    auth: process.env.GITHUB_TOKEN,
    timeZone: 'Europe/Berlin',
    baseUrl: 'https://api.github.com',
    log: {
        debug: () => { },
        info: console.info,
        warn: console.warn,
        error: console.error
    },
    request: {
        agent: undefined,
        fetch: undefined,
        timeout: 0
    }
})

function deleteBranch(branch) {
    github.request("DELETE /repos/{owner}/{repo}/git/refs/heads/{branch}", {
        owner: owner,
        repo: repo,
        branch: branch,
    }).then(console.log("Branch deleted: " + branch));
}

function handleChecked(branch, number, checks) {
    const checks_list = checks.check_runs;

    try {
        if (checks_list.length == 2 && checks_list[0].conclusion == "success" && checks_list[1].conclusion == "success") {
            console.log("Merge PR " + number);
            github.request("PUT /repos/{owner}/{repo}/pulls/{pull_number}/merge", {
                owner: owner,
                repo: repo,
                pull_number: number,
            })
        }
    }
    catch (e) {
        console.log("Branch not merged:\n " + e)
        return;
    }
    deleteBranch(branch);
}
                

function handlePull(pull) {
    const number = pull.number;
    const user = pull.user.login;
    const labels = pull.labels;
    const draft = pull.draft;
    const commit = pull.head.sha;
    const branch = pull.head.ref;
    if (user == "datengaertnerei" && draft == false) {
        console.log("Check PR " + number);
        github.request("GET /repos/{owner}/{repo}/commits/{sha}/check-runs", {
            owner: owner,
            repo: repo,
            sha: commit,
        }).then(detail => handleChecked(branch, number, detail.data));
    }
}

async function process() {

    const pulls = await github.request("GET /repos/{owner}/{repo}/pulls?state={state}", {
        owner: owner,
        repo: repo,
        state: state,
    });

    if (pulls.data.length > 0) {
        //pulls.data.forEach(handlePull);
        console.log(pulls.data);
    } else {
        console.log("no open pull requests");
    }

};

process();