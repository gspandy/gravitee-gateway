# Change Log

## [Unreleased](https://github.com/gravitee-io/gravitee-gateway/tree/HEAD)

[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.15.0...HEAD)

**Merged pull requests:**

- feat\(endpoint\): Disable automatically unavailable endpoint [\#91](https://github.com/gravitee-io/gravitee-gateway/pull/91) ([brasseld](https://github.com/brasseld))
- feat\(healthcheck\): Health-check service is calculating the endpoint s… [\#90](https://github.com/gravitee-io/gravitee-gateway/pull/90) ([brasseld](https://github.com/brasseld))

## [0.15.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.15.0) (2016-06-21)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.14.1...0.15.0)

## [0.14.1](https://github.com/gravitee-io/gravitee-gateway/tree/0.14.1) (2016-06-20)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.14.0...0.14.1)

**Merged pull requests:**

- fix\(reporter\): Update the configuration file for elasticsearch reporter [\#89](https://github.com/gravitee-io/gravitee-gateway/pull/89) ([brasseld](https://github.com/brasseld))

## [0.14.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.14.0) (2016-06-14)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.13.0...0.14.0)

**Merged pull requests:**

- Fix/\#86 trailing slash context path [\#88](https://github.com/gravitee-io/gravitee-gateway/pull/88) ([brasseld](https://github.com/brasseld))
- Fix performance issue with UUID [\#87](https://github.com/gravitee-io/gravitee-gateway/pull/87) ([brasseld](https://github.com/brasseld))
- fix small typos [\#85](https://github.com/gravitee-io/gravitee-gateway/pull/85) ([aesteve](https://github.com/aesteve))
- feat\(resource\): Add resource management [\#84](https://github.com/gravitee-io/gravitee-gateway/pull/84) ([NicolasGeraud](https://github.com/NicolasGeraud))
- fix: Resolve performance issue when handling a large amount of requests [\#83](https://github.com/gravitee-io/gravitee-gateway/pull/83) ([brasseld](https://github.com/brasseld))
- feat\(reporter\): Moving to safe LMAX disruptor to handle all reportabl… [\#82](https://github.com/gravitee-io/gravitee-gateway/pull/82) ([brasseld](https://github.com/brasseld))

## [0.13.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.13.0) (2016-05-20)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.12.0...0.13.0)

## [0.12.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.12.0) (2016-05-03)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.11.0...0.12.0)

**Merged pull requests:**

- feat\(policy\): Processing @Property annotation while loading and initi… [\#79](https://github.com/gravitee-io/gravitee-gateway/pull/79) ([brasseld](https://github.com/brasseld))
- feat\(health-check\): Refactor health-check service to use the new serv… [\#77](https://github.com/gravitee-io/gravitee-gateway/pull/77) ([brasseld](https://github.com/brasseld))

## [0.11.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.11.0) (2016-04-20)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.10.0...0.11.0)

**Merged pull requests:**

- feat\(policy\): Policy context implementation [\#74](https://github.com/gravitee-io/gravitee-gateway/pull/74) ([brasseld](https://github.com/brasseld))

## [0.10.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.10.0) (2016-04-05)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.9.0...0.10.0)

**Merged pull requests:**

- Gateway major refactoring [\#73](https://github.com/gravitee-io/gravitee-gateway/pull/73) ([brasseld](https://github.com/brasseld))

## [0.9.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.9.0) (2016-03-22)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.8.1...0.9.0)

**Merged pull requests:**

- feat\(policy\): Policy can handle streams from request / response stream [\#71](https://github.com/gravitee-io/gravitee-gateway/pull/71) ([brasseld](https://github.com/brasseld))
- Add logs to know when an API is ignored because not in configured tags [\#70](https://github.com/gravitee-io/gravitee-gateway/pull/70) ([aelamrani](https://github.com/aelamrani))

## [0.8.1](https://github.com/gravitee-io/gravitee-gateway/tree/0.8.1) (2016-03-09)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.8.0...0.8.1)

## [0.8.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.8.0) (2016-03-09)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.7.0...0.8.0)

**Merged pull requests:**

- feat\(loadblancing\): Loadbalancing algorithms must not take care about… [\#68](https://github.com/gravitee-io/gravitee-gateway/pull/68) ([brasseld](https://github.com/brasseld))
- Feat/\#63 monitoring [\#66](https://github.com/gravitee-io/gravitee-gateway/pull/66) ([brasseld](https://github.com/brasseld))
- Start / stopping an API must not deploy the current definition [\#65](https://github.com/gravitee-io/gravitee-gateway/pull/65) ([tcompiegne](https://github.com/tcompiegne))
- fix\(http\): Encode HTTP query parameters when building the request URI [\#61](https://github.com/gravitee-io/gravitee-gateway/pull/61) ([brasseld](https://github.com/brasseld))

## [0.7.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.7.0) (2016-02-22)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.6.0...0.7.0)

**Merged pull requests:**

- feature\(pipelining\): Add pipelining for HTTP client [\#57](https://github.com/gravitee-io/gravitee-gateway/pull/57) ([brasseld](https://github.com/brasseld))
- feature\(ratelimit\): Rate-limit repository proxy implementation \(gatew… [\#54](https://github.com/gravitee-io/gravitee-gateway/pull/54) ([brasseld](https://github.com/brasseld))
- Feat/\#50 failover [\#51](https://github.com/gravitee-io/gravitee-gateway/pull/51) ([brasseld](https://github.com/brasseld))

## [0.6.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.6.0) (2016-02-04)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.5.0...0.6.0)

**Merged pull requests:**

- Gateway must undeploy API if its last event type is UNPUBLISH\_API [\#49](https://github.com/gravitee-io/gravitee-gateway/pull/49) ([tcompiegne](https://github.com/tcompiegne))
- Published events are always created into the sync manager [\#45](https://github.com/gravitee-io/gravitee-gateway/pull/45) ([tcompiegne](https://github.com/tcompiegne))
- Windows Startup script added [\#41](https://github.com/gravitee-io/gravitee-gateway/pull/41) ([prvishnu](https://github.com/prvishnu))

## [0.5.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.5.0) (2016-01-20)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.4.0...0.5.0)

**Merged pull requests:**

- Features/event : Manually deploy API to the Gateway [\#31](https://github.com/gravitee-io/gravitee-gateway/pull/31) ([tcompiegne](https://github.com/tcompiegne))
- test: add/fix some tests to validate synchronization manager of gateway [\#18](https://github.com/gravitee-io/gravitee-gateway/pull/18) ([aelamrani](https://github.com/aelamrani))
- feat\(tags\): allows to manage sharding on Gateway instances from confi… [\#17](https://github.com/gravitee-io/gravitee-gateway/pull/17) ([aelamrani](https://github.com/aelamrani))
- feat\(invoker\): An invoker is a new concept to call remote API or doin… [\#15](https://github.com/gravitee-io/gravitee-gateway/pull/15) ([brasseld](https://github.com/brasseld))
- feat\(sharding\): allows to manage sharding on Gateway instances [\#14](https://github.com/gravitee-io/gravitee-gateway/pull/14) ([aelamrani](https://github.com/aelamrani))
- fix: allows to redirect on default http port when undefined on endpoi… [\#13](https://github.com/gravitee-io/gravitee-gateway/pull/13) ([aelamrani](https://github.com/aelamrani))

## [0.4.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.4.0) (2015-12-08)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.3.0...0.4.0)

## [0.3.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.3.0) (2015-11-23)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.2.0...0.3.0)

## [0.2.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.2.0) (2015-11-01)
[Full Changelog](https://github.com/gravitee-io/gravitee-gateway/compare/0.1.0...0.2.0)

## [0.1.0](https://github.com/gravitee-io/gravitee-gateway/tree/0.1.0) (2015-10-20)
**Merged pull requests:**

- feat\(repository\): Repository are now scoped : management / ratelimit … [\#11](https://github.com/gravitee-io/gravitee-gateway/pull/11) ([brasseld](https://github.com/brasseld))
- Added missing license header [\#7](https://github.com/gravitee-io/gravitee-gateway/pull/7) ([mathieuboniface](https://github.com/mathieuboniface))
- refactor: Created a new module for MongoDB registry [\#1](https://github.com/gravitee-io/gravitee-gateway/pull/1) ([aelamrani](https://github.com/aelamrani))



\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*