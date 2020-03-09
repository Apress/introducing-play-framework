package config;

import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class FilterConfig extends DefaultHttpFilters {

    @Inject
    public FilterConfig(ApplicationFilter logging) {
        super(logging);
    }
}
