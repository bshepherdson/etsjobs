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


