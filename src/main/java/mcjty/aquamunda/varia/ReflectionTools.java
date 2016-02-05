package mcjty.aquamunda.varia;



import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionTools {

    public static void setFinalStatic(Class clazz, String obfuscatedName, String fieldName, Object newValue) {
        try {
            Field field = ReflectionHelper.findField(clazz, obfuscatedName, fieldName);
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void unfinalizeField(Field field) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Field modifiersField = null;
        try {
            if (modifiersField == null) {
                modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
            }

            modifiersField.setInt(field,field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            throw e;
        }
    }
    public static void setField(Class clazz, Object instance, String obfuscatedName, String fieldName, Object value) {
        Field field = ReflectionHelper.findField(clazz, obfuscatedName, fieldName);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getField(Class clazz, Object instance, String obfuscatedName, String fieldName) {
        Field field = ReflectionHelper.findField(clazz, obfuscatedName, fieldName);
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getField(Class clazz, Object instance, String obfuscatedName) {
        Field field = ReflectionHelper.findField(clazz, obfuscatedName);
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
