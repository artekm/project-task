package pl.itacademy.schedule.holidays;

import java.util.function.Supplier;

public enum HolidaysProviderType {
    CALENDARIFIC(HolidaysFromCalendarific::new),
    ENRICO(HolidaysFromEnrico::new),
    RULE(HolidaysByRule::new),
    NONE(HolidaysNone::new);

    private Supplier<HolidaysProvider> supplier;

    HolidaysProviderType(Supplier<HolidaysProvider> supplier) {
        this.supplier = supplier;
    }

    public static HolidaysProviderType getByName(String name) {
        for (HolidaysProviderType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Wrong holidays provider type!");
    }

    public HolidaysProvider getHolidaysProviderInstance() {
        return supplier.get();
    }
}
