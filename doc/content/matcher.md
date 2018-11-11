# The Matcher

The matcher allows models to have different appearance based on a state (e.g. IBlockState for blocks).
It's basically the equivalent of the "variants"/"defaults" blocks in a vanilla blockstate definition, but a lot more versatile.
As opposed to a vanilla blockstate definition, the matcher can be used on individual values, and it isn't necessary to define
values for all existing variants. Instead, it will act more like Java's `switch` statement. You define conditions that have to
be fulfilled for the value in the match arm to be taken. You also have to provide a "default" branch whose value gets taken when
none of the other branches match.

Note, that this is not available for every value. Refer to documentation to find out where it is available.

Example:

Constant value:
```json
{ "my_value": 16.5 }
```

In this case, "my_value" will always have the value 16.5.

Matcher:
```json
{
  "my_value: match": {
    "property1=true": 1.0,
    "property1=false,property2=true": 5.0,
    "default": 16.5
  }
}
```

In this case, "my_value" will:
 - have the value 1.0 if `property1` has the value `true`,
 - have the value 5.0 if `property1` has the value `false`, and `property2` has the value `true`,
 - if nothing else matches, have the value 16.5.