package ru.boomearo.menuinv.objects;

import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.InventoryCreationHandler;
import ru.boomearo.menuinv.api.PluginPage;
import ru.boomearo.menuinv.api.PluginTemplatePages;
import ru.boomearo.menuinv.api.TemplatePage;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.exceptions.MenuInvException;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет реализацию шаблона страниц плагина
 */
public class PluginTemplatePagesImpl implements PluginTemplatePages {

    private final JavaPlugin plugin;
    private final Map<String, TemplatePageImpl> pages = new HashMap<>();

    public PluginTemplatePagesImpl(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public TemplatePageImpl getTemplatePage(String name) {
        return this.pages.get(name);
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public TemplatePage createTemplatePage(String name, InventoryCreationHandler titleHandler) throws MenuInvException {
        return createTemplatePage(name, InvType.CHEST_9X6, titleHandler);
    }

    @Override
    public TemplatePage createTemplatePage(String name, InvType type, InventoryCreationHandler titleHandler) throws MenuInvException {
        if (name == null || titleHandler == null || type == null) {
            throw new MenuInvException("Указанные аргументы являются нулевыми!");
        }

        TemplatePage tmp = this.pages.get(name);
        if (tmp != null) {
            throw new MenuInvException("Страница '" + name + "' уже создана!");
        }

        TemplatePageImpl newPage = new TemplatePageImpl(name, type, titleHandler, this);

        this.pages.put(name, newPage);

        MenuInv.getInstance().getLogger().info("Плагин '" + this.plugin.getName() + "' успешно зарегистрировал страницу '" + name + "'. Тип страницы '" + type.name());

        return newPage;
    }

    @Override
    public TemplatePage createTemplatePage(PluginPage pluginPage, InventoryCreationHandler titleHandler) throws MenuInvException {
        return createTemplatePage(pluginPage.getPage(), titleHandler);
    }

    @Override
    public TemplatePage createTemplatePage(PluginPage pluginPage, InvType type, InventoryCreationHandler titleHandler) throws MenuInvException {
        return createTemplatePage(pluginPage.getPage(), type, titleHandler);
    }

}
