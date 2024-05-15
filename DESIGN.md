# Notes on server design

Users are uniquely identified by the profile ID, which shouldn't be duplicated
across games since it's random or a hashed value or something.

Each profile exists in a particular game and has progress on certain
achievements.

## Schema

### Achievement Progress

There are a few common types of achievements, plus a few random ones.
Generally achievements are a separate table with a ref to the profile:

```
{:progress/profile [:profile/id "426973686F7032"]
 :progress/achievement :achievement/concrete-jungle   ; Enum :db/idents
 :progress/complete? true ; Usually not set rather than false.
 ...
 }
```

- "Complete N deliveries of a certain type", eg. Concrete Jungle.
  - Just `{:progress/count 6}`
- "Deliver to/from each location in this list" eg. Like a Farmer.
  - `many` field, `{:progress/location "name"}`, usually the city?
  - Some of these have complex locations, eg. two locations in the same city
    with the regular company name plus "_a" or something.
- One-shots, eg. Volvo Trucks Lover. Just the `:profile/complete?` field.
- Sets of locations to drive between. (eg. Along the Black Sea, Along the Snake
  River) These can be done in any order, and the stretches are named by
  convention in alphabetical order, eg. `"burgas-istanbul"`.

### Special Cases

A few require special handling beyond the usual types above.

#### Orient Express

Can be stored like "Along the Black Sea" but they have to go in order, and only
one way. Only the next stage of the chain is open at any one time. Steam shows
the progress bar for this.

#### Industry Standard

This would be a standard "each location" except that it's *twice* to each. Since
Datomic uses set semantics, just doubling up doesn't work.
Instead, another reverse reference:

```
{:standard/progress eid-progress
 :standard/location "pleven"
 :standard/count    2}
```

#### Some Like it Salty

This is actually a standard "each location in this list" - tweaked company names
are used for the multiple locations.

Perhaps I should capture that and mark them as "(east)" or "(west)" in the
table?

#### Energy From Above

Basic list of deliveries, but to "location/cargo".


## Digging into the profile for progress

Interesting singletons:

- `bank` 1
- `bank_loan` 1
- `delivery_log` 1
- `driver_player` 1
- `economy` 1
- `economy_event_queue` 1
- `ferry_log` 1
- `game_progress` 1
- `player` 1
- `player_job` 1
- `oversize_offer_ctrl` 1
- `registry` 1
- `trailer` 1

Other singletons

- `bus_job_log` 1
- `mail_ctrl` 1
- `police_ctrl` 1

Interesting manies:

- `company` 1246
- `delivery_log_entry` 166
- `bus_stop` 109
- `economy_event` 4334
- `ferry_log_entry` 28
- `job_info` 341
- `job_offer_data` 4225
- `map_action` 16
- `oversize_offer` 98
- `oversize_route_offers` 49
- `profit_log` 551
- `profit_log_entry` 292
- `transport_data` 3

Other manies:

- `driver_ai` 341
- `garage` 202
- `gps_waypoint_storage` 2
- `mail_def` 6
- `vehicle` 7
- `vehicle_accessory` 50
- `vehicle_addon_accessory` 89
- `vehicle_paint_job_accessory` 8
- `vehicle_wheel_accessory` 63


### Breaking down some structures in more detail

#### Economy
