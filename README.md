[![Apache License 2.0](https://img.shields.io/badge/license-apache2-red.svg?style=flat-square)](http://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.aherscu.qa/qa-automation/badge.svg)](https://search.maven.org/search?q=dev.aherscu)
[![Maven Build](https://github.com/adrian-herscu/qa-automation/actions/workflows/on-main-push.yml/badge.svg)](https://github.com/QA-Automation-Starter/qa-automation/actions)
[![Open Source Helpers](https://www.codetriage.com/adrian-herscu/qa-automation/badges/users.svg)](https://www.codetriage.com/adrian-herscu/qa-automation)

>> **[Usage instructions and Brief introduction](https://qa-automation-starter.aherscu.dev)**
> 
> (this page is for developing and maintaining this project)

# Development Instructions

## Maven Settings

Either copy [development-maven-settings.xml](development-maven-settings.xml) to
your `~/.m2` as `settings.xml`, or run Maven with
`mvn -settings development-maven-settings.xml` from this directory.

## IntelliJ Configuration

Project settings are shared via `.idea` folder
see <https://www.jetbrains.com/help/idea/creating-and-managing-projects.html#share-project-through-vcs>
and <https://www.jetbrains.com/help/idea/sharing-your-ide-settings.html#settings-repository>

## Java Code Formatting

Formatting rules are stored in
[code-formatter-rules.xml](code-formatter-rules.xml) file.

In IntelliJ, this file should be imported via the Eclipse Code Formatter plugin
(which should be installed as prerequisite).

In Eclipse, this file is supported natively.

Maven builds, by default, format the code, unless launched with
'mode-build-fast' profile.

## Testing

`mvn` on the root project will run all tests.

## Deploying

By pushing or merging into main branch.
See the [deploy-for-jdk8](.github/workflows/on-main-push.yml).

## Releasing/Versioning

We use [SemVer](http://semver.org/) for versioning.

To initiate a release, run the
[Release](.github/workflows/release.yml) action.

The above has the following effects:

* `SNAPSHOT` suffix will be removed
* sources will be committed and tagged according to current version
* artifacts will be deployed to [OSSRH](https://s01.oss.sonatype.org)
* versions will be bumped up forming a new SNAPSHOT
* a new commit will be made on main branch
* after few hours released artifacts will appear on Maven Central
  at https://search.maven.org/search?q=dev.aherscu

## Known Issues

see [docs/KNOWN-ISSUES.md](docs/KNOWN-ISSUES.md)

## Contributing

Please read [CONTRIBUTING.md](.github/CONTRIBUTING.md) for details on our
process for submitting pull requests to us, and please ensure you follow
the [CODE_OF_CONDUCT.md](.github/CODE_OF_CONDUCT.md).

## License

This project is licensed under the Apache License - see
the [LICENSE](LICENSE) file for details.

## GPG Public Key

For validating published artifacts, use
<https://keys.openpgp.org/search?q=39F1B2495B0260B2D974C634F89B5DBA3AF082E0>

## Acknowledgments

[![JetBrains Logo (Main) logo](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)](https://www.jetbrains.com/community/opensource/#support)
[<img src="https://jgiven.org/img/logo.png" height="80" alt="JGiven">](https://jgiven.org)
