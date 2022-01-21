# ETS2 Achievement Hunter

I got tired of searching for 10 different companies, and paging through jobs in
the ETS2 UI, looking for jobs that meet the conditions of various achievements.

This tool examines your save file (read-only - it doesn't change the file!) and
outputs a list of all the relevant jobs as a web page.

## Usage

**First step: Your profile must be local, not Steam Cloud!**

Open the Edit Profile screen and disable Steam Cloud.
Sorry, it's unavoidable. With Steam Cloud enabled the save files aren't actually
stored on your computer at all.

Then to actually use this tool:

- Get a JAR from the Releases page.
- Have Java 13 or later installed.
- Run the JAR (in any folder) by either:
  - Double-clicking it, or
  - The command `java -jar ets_achievements.jar`
- Visit [`http://localhost:8483`](http://localhost:8483) in a browser.
  - From another device like a phone, you can use your computer's internal IP
    address, generally `192.168.*.*`.

This shows a list of profiles by name. Choose your desired profile to get the
list of jobs.

### On Freshness

The tool reads your most recent save based on the file timestamps. You can do a
manual save or quicksave to ensure you have the latest. Note that the game
autosaves when you finish a job, so usually when you're looking for a new one
you'll have fresh data.

As a sanity check, the current in-game time is displayed at the top.

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
be done in order, only one of the city pairs is open at a time. You'll have to
keep track of that.

A few are missing because I haven't gotten around to implementing them yet.

- "Taste the Sun" is missing because I don't have a list of ADR cargoes.
- "Grand Tour" is not implemented because it would list way too many jobs to be
  useful. That one isn't hard to search for.


## Future Work

There are four main lines of future work:

- Adding the missing achievements.
- ATS support.
- Stretching: ordering jobs by proximity to the player, or by some kind of
  `distance / expiration time` "plausibility" metric. This lists many jobs that
  are not practical because you're far away, or you're close but there's hardly
  any time left.

