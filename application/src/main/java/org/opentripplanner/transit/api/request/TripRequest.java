package org.opentripplanner.transit.api.request;

import java.time.LocalDate;
import java.util.Objects;
import org.opentripplanner.transit.api.model.FilterValues;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.timetable.Trip;
import org.opentripplanner.utils.tostring.ToStringBuilder;

/**
 * A request for {@link Trip}s.
 * </p>
 * This request is used to retrieve {@link Trip}s that match the provided filter values.
 */
public class TripRequest {

  private final FilterValues<FeedScopedId> includeAgencies;
  private final FilterValues<FeedScopedId> includeRoutes;

  private final FilterValues<FeedScopedId> excludeAgencies;
  private final FilterValues<FeedScopedId> excludeRoutes;

  private final FilterValues<FeedScopedId> excludeStops;
  private final FilterValues<FeedScopedId> includeStops;

  private final FilterValues<String> includeNetexInternalPlanningCodes;
  private final FilterValues<LocalDate> includeServiceDates;

  TripRequest(
    FilterValues<FeedScopedId> includeAgencies,
    FilterValues<FeedScopedId> includeRoutes,
    FilterValues<FeedScopedId> includeStops,
    FilterValues<FeedScopedId> excludeAgencies,
    FilterValues<FeedScopedId> excludeRoutes,
    FilterValues<FeedScopedId> excludeStops,
    FilterValues<String> includeNetexInternalPlanningCodes,
    FilterValues<LocalDate> includeServiceDates
  ) {
    this.includeAgencies = includeAgencies;
    this.includeRoutes = includeRoutes;
    this.includeStops = includeStops;
    this.excludeAgencies = excludeAgencies;
    this.excludeRoutes = excludeRoutes;
    this.excludeStops = excludeStops;
    this.includeNetexInternalPlanningCodes = includeNetexInternalPlanningCodes;
    this.includeServiceDates = includeServiceDates;
  }

  public static TripRequestBuilder of() {
    return new TripRequestBuilder();
  }

  public FilterValues<FeedScopedId> includeAgencies() {
    return includeAgencies;
  }

  public FilterValues<FeedScopedId> includeRoutes() {
    return includeRoutes;
  }

  public FilterValues<FeedScopedId> includeStops() {
    return includeStops;
  }

  public FilterValues<FeedScopedId> excludeAgencies() {
    return excludeAgencies;
  }

  public FilterValues<FeedScopedId> excludeRoutes() {
    return excludeRoutes;
  }

  public FilterValues<FeedScopedId> excludeStops() {
    return excludeStops;
  }

  public FilterValues<String> includeNetexInternalPlanningCodes() {
    return includeNetexInternalPlanningCodes;
  }

  public FilterValues<LocalDate> includeServiceDates() {
    return includeServiceDates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TripRequest that)) return false;
    return (
      Objects.equals(includeAgencies, that.includeAgencies) &&
      Objects.equals(includeRoutes, that.includeRoutes) &&
      Objects.equals(includeStops, that.includeStops) &&
      Objects.equals(excludeAgencies, that.excludeAgencies) &&
      Objects.equals(excludeRoutes, that.excludeRoutes) &&
      Objects.equals(excludeStops, that.excludeStops) &&
      Objects.equals(includeNetexInternalPlanningCodes, that.includeNetexInternalPlanningCodes) &&
      Objects.equals(includeServiceDates, that.includeServiceDates)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(
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

  @Override
  public String toString() {
    return ToStringBuilder.of(TripRequest.class)
      .addObj("includeAgencies", includeAgencies)
      .addObj("includeRoutes", includeRoutes)
      .addObj("includeStops", includeStops)
      .addObj("excludeAgencies", excludeAgencies)
      .addObj("excludeRoutes", excludeRoutes)
      .addObj("excludeStops", excludeStops)
      .addObj("includeNetexInternalPlanningCodes", includeNetexInternalPlanningCodes)
      .addObj("includeServiceDates", includeServiceDates)
      .toString();
  }
}
