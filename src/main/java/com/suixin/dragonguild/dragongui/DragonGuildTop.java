package com.suixin.dragonguild.dragongui;

import com.suixin.dragonguild.entity.DragonGuildEntity;
import com.suixin.dragonguild.handler.DragonGuildDatabaseHandler;
import com.suixin.dragonguild.handler.DragonGuildMemBerDatabaseHandler;
import com.suixin.dragonguild.util.*;
import eos.moe.dragoncore.api.easygui.EasyScreen;
import eos.moe.dragoncore.api.easygui.component.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;


public class DragonGuildTop {
    //创建GUI
    public static EasyScreen getGui() {
        YamlConfiguration mainGui = DragonGuiYml.getBackground();
        return new EasyScreen(ImageUrlEnum.background.getUrl(), mainGui.getInt("width"), mainGui.getInt("high"));
    }

    //打开GUI
    public static void openGameLobbyGui(Player player,Integer dragonGuildId) {
        EasyScreen gui = createGui(player,dragonGuildId);
        gui.openGui(player);
    }

    //创建组件
    public static EasyScreen createGui(Player player, Integer dragonGuildId) {
        EasyScreen screen = getGui();
        //大厅
        YamlConfiguration lobby = DragonGuiYml.getLobby();
        EasyButton lobbyButton = new EasyButton(lobby.getInt("x"), lobby.getInt("y"), lobby.getInt("width"), lobby.getInt("high"), ImageUrlEnum.lobby.getUrl(), PImageUrlEnum.lobby.getUrl()) {
            @Override
            public void onClick(Player player, Type type) {
                DragonGuildGui.openGameLobbyGui(player);
            }
        };
        //公告
        YamlConfiguration notice = DragonGuiYml.getNotice();
        EasyButton noticeButton = new EasyButton(notice.getInt("x"), notice.getInt("y"), notice.getInt("width"), notice.getInt("high"), ImageUrlEnum.notice.getUrl(), PImageUrlEnum.notice.getUrl()) {
            @Override
            public void onClick(Player player, Type type) {
                DragonGuildNotice.openGameLobbyGui(player,dragonGuildId);
            }
        };
        //聊天
        YamlConfiguration chat = DragonGuiYml.getChat();
        EasyButton chatButton = new EasyButton(chat.getInt("x"), chat.getInt("y"), chat.getInt("width"), chat.getInt("high"), ImageUrlEnum.chat.getUrl(), PImageUrlEnum.chat.getUrl()) {
            @Override
            public void onClick(Player player, Type type) {
                DragonGuildChat.openGameLobbyGui(player,dragonGuildId);
            }
        };
        //审批
        YamlConfiguration apply = DragonGuiYml.getApply();
        EasyButton applyButton = new EasyButton(apply.getInt("x"), apply.getInt("y"), apply.getInt("width"), apply.getInt("high"), ImageUrlEnum.apply.getUrl(), PImageUrlEnum.apply.getUrl()) {
            @Override
            public void onClick(Player player, Type type) {
                DragonGuildApply.openGameLobbyGui(player,dragonGuildId);
            }
        };
        //排行
        YamlConfiguration top = DragonGuiYml.getTop();
        EasyButton topButton = new EasyButton(top.getInt("x"), top.getInt("y"), top.getInt("width"), top.getInt("high"), PImageUrlEnum.top.getUrl(), PImageUrlEnum.top.getUrl()) {
            @Override
            public void onClick(Player player, Type type) {
                DragonGuildTop.openGameLobbyGui(player,dragonGuildId);
            }
        };
        //关闭
        YamlConfiguration close = DragonGuiYml.getClose();
        EasyButton closeButton = new EasyButton( close.getInt("x"), close.getInt("y"), close.getInt("width"), close.getInt("high"), ImageUrlEnum.close.getUrl(), PImageUrlEnum.close.getUrl() ) {
            @Override
            public void onClick(Player player, Type type) {
                player.closeInventory();
            }
        };
        Map<String, Component> userComponent = DragonGuildGui.getUserComponent();
        Component component = userComponent.get(player.getName());
        YamlConfiguration topbgYml = DragonGuiYml.getTopbg();
        EasyScrollingList scrollingList = new EasyScrollingList(topbgYml.getInt("x"), topbgYml.getInt("y"), topbgYml.getInt("width"), topbgYml.getInt("high"), ImageUrlEnum.listbg.getUrl());
        YamlConfiguration barYml = DragonGuiYml.getBar();
        scrollingList.setBar(barYml.getInt("w"), barYml.getInt("h"), barYml.getInt("high"), ImageUrlEnum.bar.getUrl());
        easyScrollList(scrollingList,player, component, 1);
        component.setScrollingList(scrollingList);
        //上一页
        YamlConfiguration shangyiye = DragonGuiYml.getTopShangyiye();
        EasyButton shangyiyeButton = new EasyButton( shangyiye.getInt("x"), shangyiye.getInt("y"), shangyiye.getInt("width"), shangyiye.getInt("high"), ImageUrlEnum.shangyiye.getUrl(), PImageUrlEnum.shangyiye.getUrl()) {
            @Override
            public void onClick(Player player, Type type) {
                Component component = DragonGuildGui.getUserComponent().get(player.getName());
                Integer currentPage = component.getCurrent();
                if (currentPage == 1) {
                    return;
                }
                EasyScreen openedScreen = EasyScreen.getOpenedScreen(player);
                EasyScrollingList scrollingList1 = component.getScrollingList();
                easyScrollList(scrollingList1,player, component, currentPage - 1);
                openedScreen.addComponent(scrollingList1);
                userComponent.put(player.getName(),component);
                openedScreen.updateGui(player);
            }
        };

        //下一页
        YamlConfiguration xiayiye = DragonGuiYml.getTopXiayiye();
        EasyButton xiayiyeButton = new EasyButton( xiayiye.getInt("x"), xiayiye.getInt("y"), xiayiye.getInt("width"), xiayiye.getInt("high"),ImageUrlEnum.xiayiye.getUrl(), PImageUrlEnum.xiayiye.getUrl() ) {
            @Override
            public void onClick(Player player, Type type) {
                Component component = DragonGuildGui.getUserComponent().get(player.getName());
                Integer limit = component.getCurrent() + 1;
                EasyScreen openedScreen = EasyScreen.getOpenedScreen(player);
                EasyScrollingList scrollingList1 = component.getScrollingList();
                easyScrollList(scrollingList1,player, component, limit);
                openedScreen.addComponent(scrollingList1);
                userComponent.put(player.getName(),component);
                openedScreen.updateGui(player);
            }
        };
        YamlConfiguration listBgkYml = DragonGuiYml.getListBgk();
        EasyImage listBgkImg = new EasyImage( listBgkYml.getInt("x"), listBgkYml.getInt("y"), listBgkYml.getInt("width"), listBgkYml.getInt("high"),ImageUrlEnum.listbgk.getUrl());
        DragonGuildEntity dragonGuildEntity = DragonGuildDatabaseHandler.selectDragonGuildById(dragonGuildId);
        YamlConfiguration name = DragonGuiYml.getName();
        EasyLabel nameText = new EasyLabel(name.getInt("x"), name.getInt("y"), 1, Arrays.asList(dragonGuildEntity.getName()));
        Integer count = DragonGuildMemBerDatabaseHandler.selectCount(dragonGuildId);
        YamlConfiguration renshu = DragonGuiYml.getRenshu();
        YamlConfiguration level = DragonGuiYml.getLevel();
        EasyLabel renshuText = new EasyLabel(renshu.getInt("x"), renshu.getInt("y"), 1, Arrays.asList("成员:"+count + "/"+dragonGuildEntity.getMaxMember()));
        EasyLabel levelText = new EasyLabel( level.getInt("x"), level.getInt("y"),1, Arrays.asList("等级:"+dragonGuildEntity.getLevel()+""));
        //图标
        YamlConfiguration guildImgYml = DragonGuiYml.getGuildImg();
        EasyImage guildImg = new EasyImage( guildImgYml.getInt("x"), guildImgYml.getInt("y"), guildImgYml.getInt("width"), guildImgYml.getInt("high"),ImageUrlEnum.guildImg.getUrl());
        screen.addComponent(listBgkImg);
        screen.addComponent(scrollingList);
        screen.addComponent(guildImg);
        screen.addComponent(nameText);
        screen.addComponent(renshuText);
        screen.addComponent(levelText);
        screen.addComponent(nameText);
        screen.addComponent(lobbyButton);
        screen.addComponent(noticeButton);
        screen.addComponent(chatButton);
        screen.addComponent(applyButton);
        screen.addComponent(topButton);
        screen.addComponent(closeButton);
        screen.addComponent(shangyiyeButton);
        screen.addComponent(xiayiyeButton);
        return screen;
    }

    private static void easyScrollList(EasyScrollingList scrollingList,Player player, Component component, Integer limit) {
        int currentPage = limit;
        //获取页数
        EasyScreen openedScreen = EasyScreen.getOpenedScreen(player);
        Map<String, EasyComponent> components = new HashMap<>();
        if (openedScreen != null) {
            components = openedScreen.getComponents();
        }
        limit = (limit - 1) * 6;
        List<EasyComponent> list = new ArrayList<>();
        List<String> guildList = component.getGuildList();
        YamlConfiguration topYml = DragonGuiYml.getTopList();
        YamlConfiguration topImgYml = DragonGuiYml.getTopImg();
        YamlConfiguration nameYml = DragonGuiYml.getTopName();
        YamlConfiguration topLevelYml = DragonGuiYml.getTopLevel();
        int topYmlY = topYml.getInt("y");
        int topImgYmlY = topImgYml.getInt("y");
        int nameYmlY = nameYml.getInt("y");
        int topLevelYmlY = topLevelYml.getInt("y");
        List<DragonGuildEntity> dragonGuildEntities = DragonGuildDatabaseHandler.selectDragonGuildDataNum(limit);
        if (dragonGuildEntities.size() == 0) {
            player.sendMessage(Message.no_more_guild);
            return;
        }
        for (String easyComponent : guildList) {
            components.remove(easyComponent);
        }
        for (int i = 0; i < dragonGuildEntities.size(); i ++) {
            String name = dragonGuildEntities.get(i).getName();
            Integer level = dragonGuildEntities.get(i).getLevel();
            EasyImage topUmg = new EasyImage( topYml.getInt("x"), topYmlY, topYml.getInt("width"), topYml.getInt("high"),ImageUrlEnum.guildList.getUrl());
            EasyLabel nameText = new EasyLabel(nameYml.getInt("x"), nameYmlY, 1,Arrays.asList(name));
            EasyLabel topLevelText = new EasyLabel(topLevelYml.getInt("x"), topLevelYmlY, 1,Arrays.asList("LV: "+level));
            EasyImage img;
            if (currentPage == 1 && i == 0) {
                img = new EasyImage( topImgYml.getInt("x"), topImgYmlY, topImgYml.getInt("width"), topImgYml.getInt("high"),ImageUrlEnum.guild.getUrl());
            }else if (currentPage == 1 && i == 1) {
                img = new EasyImage( topImgYml.getInt("x"), topImgYmlY, topImgYml.getInt("width"), topImgYml.getInt("high"),ImageUrlEnum.guild2.getUrl());
            }else if (currentPage == 1 && i == 2) {
                img = new EasyImage( topImgYml.getInt("x"), topImgYmlY, topImgYml.getInt("width"), topImgYml.getInt("high"),ImageUrlEnum.guild3.getUrl());
            }else{
                img = new EasyImage( topImgYml.getInt("x"), topImgYmlY, topImgYml.getInt("width"), topImgYml.getInt("high"),ImageUrlEnum.guild4.getUrl());
            }
            list.add(topUmg);
            list.add(nameText);
            list.add(img);
            list.add(topLevelText);
            scrollingList.addComponent(topUmg);
            scrollingList.addComponent(nameText);
            scrollingList.addComponent(img);
            scrollingList.addComponent(topLevelText);
            topYmlY = topYmlY + topYml.getInt("interval");
            nameYmlY = nameYmlY + topYml.getInt("interval");
            topImgYmlY = topImgYmlY + topYml.getInt("interval");
            topLevelYmlY = topLevelYmlY + topYml.getInt("interval");
        }
        for (EasyComponent easyComponent : list) {
            guildList.add(easyComponent.getId());
        }
        component.setCurrent(currentPage);
        component.setGuildList(guildList);
        Map<String, Component> userComponent = DragonGuildGui.getUserComponent();
        userComponent.put(player.getName(),component);
    }
}