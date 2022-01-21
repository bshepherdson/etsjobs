# ETS2 Achievement Hunter

I got tired of searching for 10 different companies, and paging through jobs in
the ETS2 UI, looking for jobs that meet the conditions of various achievements.

This tool examines your save file (read-only - it doesn't change the file!) and
outputs a list of all the relevant jobs.

## Usage

**First step: Your profile must be local, not Steam Cloud!**

You can Edit Profile and set that up. Sorry, it's unavoidable. The save files
aren't stored on your computer at all, with Steam Cloud enabled.

Then to actually use this tool:

- Get a JAR from the Releases page.
- Have Java 13 or later installed.
- Run the JAR as

```
java -jar ets_achievements.jar \
  "path/to/Euro Truck Simulator 2/profiles" "MyProfileName"
```

The tool will read the basic data of all the profiles, find the one with the
matching name, find the most-recently-written save file for that profile, decode
it, and print out all the currently available jobs that match any achievement's
requirements.

## Compatibility

This works on the latest versions of ETS2 as of January 2022. It works with all
map expansion DLCs, and should work with most mods (eg. ProMods) though some
things might be a bit off.

(Eg. company sites that belong to a different city in ProMods will be listed
under their "stock" location. Likewise it uses the stock names for companies and
cargoes, even if a mod has changed them. IKA, not IKEA, etc.)

## Achievements

This is focused on the "Deliver to all the marinas in Scandinavia" type of job,
not on economic ones (hiring AI drivers, making profit as a company) or those
that don't require particular jobs (use all ferries, discover all cities, view
all landmarks).

### Caveats

"Orient Express" lists all jobs that *can* apply for it, but since they have to
be done in order, only one of the city pairs is open at a time.

A few are missing because I haven't gotten around to implementing them yet.

- "Taste the Sun" is missing because I don't have a list of ADR cargoes.
- "Grand Tour" is not implemented because it would list way too many jobs to be
  useful. That one isn't hard to search for.


## Future Work

There are four main lines of future work:

- Adding the missing achievements.
- I want to make this more accessible by having it host a web server that offers
  a more intuitive interface (including hiding achievements you've already got).
- ATS support.
- Stretching: ordering jobs by proximity to the player, or by some kind of
  `distance / expiration time` "plausibility" metric. This lists many jobs that
  are not practical because you're far away, or you're close but there's hardly
  any time left.

