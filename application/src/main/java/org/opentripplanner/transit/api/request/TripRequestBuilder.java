package org.opentripplanner.transit.api.request;

import java.time.LocalDate;
import java.util.List;
import javax.annotation.Nullable;
import org.opentripplanner.transit.api.model.FilterValues;
import org.opentripplanner.transit.model.framework.FeedScopedId;

public class TripRequestBuilder {

  private FilterValues<FeedScopedId> includeAgencies = FilterValues.ofNullIsEverything(
    "includeAgencies",
    null
  );

  private FilterValues<FeedScopedId> excludeAgencies = FilterValues.ofEmptyIsEverything(
    "excludeAgencies",
    List.of()
  );
  private FilterValues<FeedScopedId> excludeRoutes = FilterValues.ofEmptyIsEverything(
    "excludeRoutes",
    List.of()
  );

  private FilterValues<FeedScopedId> includeRoutes = FilterValues.ofNullIsEverything(
    "includeRoutes",
    null
  );

  private FilterValues<FeedScopedId> excludeStops = FilterValues.ofEmptyIsEverything(
    "excludeStops",
    List.of()
  );

  private FilterValues<FeedScopedId> includeStops = FilterValues.ofNullIsEverything(
    "includeStops",
    null
  );

  private FilterValues<String> includeNetexInternalPlanningCodes = FilterValues.ofNullIsEverything(
    "includeNetexInternalPlanningCodes",
    null
  );
  private FilterValues<LocalDate> includeServiceDates = FilterValues.ofNullIsEverything(
    "includeServiceDates",
    null
  );

  TripRequestBuilder() {}

  public TripRequestBuilder withIncludeAgencies(@Nullable List<FeedScopedId> includeAgencies) {
    this.includeAgencies = FilterValues.ofNullIsEverything("includeAgencies", includeAgencies);
    return this;
  }

  public TripRequestBuilder withIncludeRoutes(@Nullable List<FeedScopedId> includeRoutes) {
    this.includeRoutes = FilterValues.ofNullIsEverything("includeRoutes", includeRoutes);
    return this;
  }

  public TripRequestBuilder withIncludeStops(@Nullable List<FeedScopedId> includeStops) {
    this.includeStops = FilterValues.ofNullIsEverything("includeStops", includeStops);
    return this;
  }

  public TripRequestBuilder withExcludeAgencies(@Nullable List<FeedScopedId> agencies) {
    this.excludeAgencies = FilterValues.ofEmptyIsEverything("excludedAgencies", agencies);
    return this;
  }

  public TripRequestBuilder withExcludeRoutes(@Nullable List<FeedScopedId> routes) {
    this.excludeRoutes = FilterValues.ofEmptyIsEverything("excludedRoutes", routes);
    return this;
  }

  public TripRequestBuilder withExcludeStops(@Nullable List<FeedScopedId> stops) {
    this.excludeStops = FilterValues.ofEmptyIsEverything("excludedStops", stops);
    return this;
  }

  public TripRequestBuilder withIncludeNetexInternalPlanningCodes(
    @Nullable List<String> includeNetexInternalPlanningCodes
  ) {
    this.includeNetexInternalPlanningCodes = FilterValues.ofNullIsEverything(
      "includeNetexInternalPlanningCodes",
      includeNetexInternalPlanningCodes
    );
    return this;
  }

  public TripRequestBuilder withIncludeServiceDates(@Nullable List<LocalDate> includeServiceDates) {
    this.includeServiceDates = FilterValues.ofNullIsEverything(
      "includeServiceDates",
      includeServiceDates
    );
    return this;
  }

  public TripRequest build() {
    return new TripRequest(
      includeAgencies,
      includeRoutes,
      includeStops,
      excludeAgencies,
      excludeRoutes,
      excludeStops,
      includeNetexInternalPlanningCodes,
      includeServiceDates
    );
  }
}
