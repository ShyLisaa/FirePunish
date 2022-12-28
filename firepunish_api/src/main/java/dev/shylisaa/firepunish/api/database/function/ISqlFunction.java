package dev.shylisaa.firepunish.api.database.function;

public interface ISqlFunction<I, O> {

    O apply(I i);
}
