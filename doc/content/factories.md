# Defining custom models/parts/transforms

You can do this by creating a `_factories.json` file in `assets/your-modid/render`.
This file is a JSON object which can contain the objects "transforms", "parts" and/or "models".

In one of those objects, add the name of your new model/part/transform, and the value is the fully qualified class name
that implements the respective factory type.

For example, if you want to create a new part "sphere", you first create a class `SphereFactory` that implements
`therealfarfetchd.qcommon.architect.factories.PartFactory`. It must have a constructor with no parameters.
`PartFactory` is an interface with one method, `parse`, which gives you a `ParseContext` and the `JsonObject` of the
part you want to parse. This method should never return null or throw an exception, instead you should log errors or warnings
through the ParseContext and return a default value if something fails so that the parsing process can complete successfully
and all parsing errors can be shown at once. Use the methods in the `JsonParserUtils` class to parse a variety of objects, with
optional [Matcher](matcher.md) support.

Once this class is implemented, merge something like this into your
`_factories.json` file (of course, you'll have to adjust it to your use case):

```json
{
  "parts": {
    "sphere": "path.to.your.SphereFactory"
  }
}
```

Now the new part should be usable as `your-modid:sphere`. If something fails, errors will be shown in the game log on startup.