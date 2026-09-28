## Overview

[![Join the chat at https://gitter.im/opentripplanner/OpenTripPLanner](https://badges.gitter.im/opentripplanner/OpenTripPlanner.svg)](https://gitter.im/opentripplanner/OpenTripPlanner)
[![Matrix](https://img.shields.io/matrix/opentripplanner%3Amatrix.org?label=Matrix%20chat&?cacheSeconds=172800)](https://matrix.to/#/#opentripplanner_OpenTripPlanner:gitter.im)
[![codecov](https://codecov.io/gh/opentripplanner/OpenTripPlanner/branch/dev-2.x/graph/badge.svg?token=ak4PbIKgZ1)](https://codecov.io/gh/opentripplanner/OpenTripPlanner)
[![Commit activity](https://img.shields.io/github/commit-activity/y/opentripplanner/OpenTripPlanner)](https://github.com/opentripplanner/OpenTripPlanner/graphs/contributors)
[![Docker Pulls](https://img.shields.io/docker/pulls/opentripplanner/opentripplanner)](https://hub.docker.com/r/opentripplanner/opentripplanner)


## About this fork

This project is a modified version of OpenTripPlanner 2.8.1, designed to reproduce trips in the
Danish National Travel Survey using scheduled public transit data.

Unlike standard OpenTripPlanner usage, the primary purpose is not route optimization, but the
reconstruction of respondents’ realized routes.

In addition, the system is used to generate choice-set of alternative routes for use in route choice models.

### What I changed

Reproduction needs the route the respondent actually took, not the route they were permitted to
take. The changes below are deliberate trade-offs for that purpose and wrong for ordinary trip
planning — this graph will route through places that are genuinely impassable. See
`git log v2.8.1..main` for the individual commits.

**Transit API**

- **`S_TRAIN` transit mode.** GTFS `route_type=109` (Copenhagen S-tog) maps to a new `S_TRAIN`
  mode instead of `RAIL`, so S-trains can be filtered separately from regional and intercity rail.
  This changes the built graph, not just the API: stock OTP tags these routes `RAIL`.
- **`routeShortNames` filter.** A new transit select/whitelist filter matching GTFS
  `route_short_name`, on both the GTFS and Transmodel APIs.

`tu_reconstruct_trips` uses both, so it will not work against a stock OTP jar.

**Street network (OSM)**

- **`access` tags ignored** — no through-traffic restrictions, and general access denial no longer
  makes a way non-routable (`OsmEntity`, `OsmNode`, `OsmTagMapper`, `BarrierEdgeBuilder`).
- **`barrier` tags ignored** — barrier nodes and ways no longer restrict permissions
  (`OsmNode`, `OsmWay`, `BarrierEdgeBuilder`).
- **Escalators treated as plain steps**, walkable in both directions regardless of `conveying=`.
  One-way escalators cut off stops — the Forum metro platform was reachable only downwards
  (`OsmWay.isEscalator()`, `EscalatorProcessor`).
- **More ways routable** — `highway=no`, `rest_area` and `services` removed from the non-routable
  list, and indoor `footway` added to the indoor-routable values (`OsmEntity`).
- **`highway=step` → `highway=steps`** in the foot-mode check (`OsmEntity`) — an upstream typo, and
  the only change here worth upstreaming.

**Parking**

- **No capacity checks** on vehicle parking — OSM often lacks the data, and cyclists park outside
  official parking anyway (`VehicleParkingEdge`).

**`DenmarkMapper` tag mapping**

- A new `osmTagMapping: "denmark"` option. It is a copy of upstream's `NorwayMapper`, differing only
  in the motorway speed (130 km/h instead of 110 km/h). Its main effect comes from the Norwegian
  rules it inherits: bicycles are allowed on footways and pedestrians on cycleways and bridleways —
  the latter matching Danish traffic rules. It only applies when the build config selects it.

## Building and running

```
mvn package     # build; produces otp-shaded/target/otp-shaded-2.8.1.jar
mvn test        # run tests
```

OTP resolves `build-config.json`, `router-config.json`, the OSM extract and the GTFS feed relative
to its **working directory**, so the working directory — not a command-line flag — decides which
graph is built or served:

```
java -Xmx100G -jar otp-shaded-2.8.1.jar --buildStreet --save .   # street graph from OSM
java -Xmx100G -jar otp-shaded-2.8.1.jar --loadStreet --save .    # transit graph on top of streetGraph.obj
java -Xmx100G -jar otp-shaded-2.8.1.jar --load .                 # serve a built graph.obj
```

From IntelliJ, run `org.opentripplanner.standalone.OTPMain` with VM options
`-Djava.util.concurrent.ForkJoinPool.common.parallelism=13 -Xmx100G` and the working directory set
as above.

Both config files may contain `${VAR}` placeholders; substitution from the process environment is
an upstream OTP feature (see `doc/user/Configuration.md`), not something this fork adds. Which
variables this project sets, and how the per-year data directories are laid out, is described in
the repository root's `README.md`.

## Original README of OTP

OpenTripPlanner (OTP) is an open source multi-modal trip planner, focusing on travel by scheduled
public transportation in combination with bicycling, walking, and mobility services including bike
share and ride hailing. Its server component runs on any platform with a Java virtual machine (
including Linux, Mac, and Windows). It exposes GraphQL APIs that can be accessed by various
clients including open source Javascript components and native mobile applications. It builds its
representation of the transportation network from open data in open standard file formats (primarily
GTFS and OpenStreetMap). It applies real-time updates and alerts with immediate visibility to
clients, finding itineraries that account for disruptions and service changes.

Note that this branch contains **OpenTripPlanner 2**, the second major version of OTP, which has
been under development since 2018 and is now the dominant one and the only one being supported.

## Performance Test

[📊 Dashboard](https://otp-performance.leonard.io/) 

We run a speed test (included in the code) to measure the performance for every PR merged into OTP. 

[More information about how to set up and run it.](./test/performance/README.md)

## Repository layout

The main Java server code is in `application/src/main/`. OTP also includes a Javascript client 
based on the MapLibre mapping library in `client/src/`. This client is now used for testing, with
most major deployments building custom clients from reusable components. The Maven build produces a
unified ("shaded") JAR file at `otp-shaded/target/otp-shaded-VERSION.jar` containing all necessary
code and dependencies to run OpenTripPlanner.

Additional information and instructions are available in
the [main documentation](http://docs.opentripplanner.org/en/dev-2.x/), including a
[quick introduction](http://docs.opentripplanner.org/en/dev-2.x/Basic-Tutorial/).

## Development


OpenTripPlanner is a collaborative project incorporating code, translation, and documentation from
contributors around the world. We welcome new contributions.
Further [development guidelines](http://docs.opentripplanner.org/en/latest/Developers-Guide/) can be
found in the documentation.

### Development history

The OpenTripPlanner project was launched by Portland, Oregon's transport agency
TriMet (http://trimet.org/) in July of 2009. As of this writing in Q3 2020, it has been in
development for over ten years. See the main documentation for an overview
of [OTP history](http://docs.opentripplanner.org/en/dev-2.x/History/) and a list
of [cities and regions using OTP](http://docs.opentripplanner.org/en/dev-2.x/Deployments/) around
the world.

## Getting in touch

The fastest way to get help is to use our [Gitter chat room](https://gitter.im/opentripplanner/OpenTripPlanner) where most of the core developers
are. Bug reports may be filed via the Github [issue tracker](https://github.com/openplans/OpenTripPlanner/issues). The OpenTripPlanner [mailing list](http://groups.google.com/group/opentripplanner-users)
is used almost exclusively for project announcements. The mailing list and issue tracker are not
intended for support questions or discussions. Please use the chat for this purpose. Other details
of [project governance](http://docs.opentripplanner.org/en/dev-2.x/Governance/) can be found in the main documentation.

## OTP Ecosystem

- [awesome-transit](https://github.com/MobilityData/awesome-transit) Community list of transit APIs,
  apps, datasets, research, and software.
