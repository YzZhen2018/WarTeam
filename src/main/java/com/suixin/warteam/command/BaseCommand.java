package com.suixin.warteam.command;

import com.suixin.warteam.WarTeam;
import com.suixin.warteam.entity.WarTeamApplyEntity;
import com.suixin.warteam.entity.WarTeamEntity;
import com.suixin.warteam.entity.WarTeamMemberEntity;
import com.suixin.warteam.gui.WarTeamGui;
import com.suixin.warteam.handler.WarTeamApplyDatabaseHandler;
import com.suixin.warteam.handler.WarTeamDatabaseHandler;
import com.suixin.warteam.handler.WarTeamMemBerDatabaseHandler;
import com.suixin.warteam.util.Message;
import com.suixin.warteam.util.VaultAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class BaseCommand implements CommandExecutor {
	HashMap<Integer, Integer> levelMap = WarTeam.getLevelMap();
	HashMap<Integer, Integer> maxNumMap = WarTeam.getMaxNumMap();
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("错误，这是一个玩家指令!");
			return true;
		}
		boolean op = player.isOp();
		if (cmd.getName().equalsIgnoreCase("wt")) {
			if (args.length == 0) {
				player.sendMessage("§c§l§m §c§l§m §c§l§m §c§l§m §6§l§m §6§l§m §6§l§m §6§l§m §e§l§m §e§l§m §e§l§m §e§l§m §a§l§m §a§l§m §a§l§m §a§l§m §b§l§m §b§l§m §b§l§m §b§l§m §b§l§m §b§l§m §a§l§m §a§l§m §a§l§m §a§l§m §e§l§m §e§l§m §e§l§m §e§l§m §6§l§m §6§l§m §6§l§m §6§l§m §c§l§m §c§l§m §c§l§m §c§l§m");
				player.sendMessage("§6●§a/wt open §e打开战队管理");
				player.sendMessage("§6●§a/wt list <页码> §e打开战队列表");
				player.sendMessage("§6●§a/wt create <战队> §e创建战队!");
				player.sendMessage("§6●§a/wt update <战队> §e修改战队名!");
				player.sendMessage("§6●§a/wt join <战队> §e加入一个战队!");
				player.sendMessage("§6●§a/wt dismiss  §e解散战队!");
				player.sendMessage("§6●§a/wt out  §e退出战队!");
				player.sendMessage("§6●§a/wt out <玩家> §e踢出战队!");
				player.sendMessage("§6●§a/wt agree <玩家> §e同意玩家加入战队!");
				player.sendMessage("§6●§a/wt refuse <玩家> §e拒绝玩家加入战队!");
				if (op) {
					player.sendMessage("§6●§a/wt reload §e重载插件!");
				}
				player.sendMessage("§c§l§m §c§l§m §c§l§m §c§l§m §6§l§m §6§l§m §6§l§m §6§l§m §e§l§m §e§l§m §e§l§m §e§l§m §a§l§m §a§l§m §a§l§m §a§l§m §b§l§m §b§l§m §b§l§m §b§l§m §b§l§m §b§l§m §a§l§m §a§l§m §a§l§m §a§l§m §e§l§m §e§l§m §e§l§m §e§l§m §6§l§m §6§l§m §6§l§m §6§l§m §c§l§m §c§l§m §c§l§m §c§l§m");
				return true;
			}
			List<String> argsList = Arrays.asList(args);
			String arg1 = argsList.get(0);
			if (arg1.equals("open")) {
				//打开战队
				WarTeamGui.openGameLobbyGui(player);
			} else if (arg1.equals("list")) {
				//打开战队列表
				warTeamList(argsList,player);
			} else if (arg1.equals("create")) {
				//创建战队
				createWarTeam(argsList,player);
			} else if (arg1.equals("join")) {
				//加入一个战队
				joinWarTeam(argsList,player);
			} else if (arg1.equals("dismiss")) {
				//解散战队
				dismissWarTeam(argsList,player);
			} else if (arg1.equals("out")) {
				//退出战队
				outWarTeam(argsList,player);
			} else if (arg1.equals("agree")) {
				//同意玩家加入战队
				applyPassWarTeam(argsList,player);
			} else if (arg1.equals("refuse")) {
				//拒绝玩家加入战队!
				applyNoPassWarTeam(argsList,player);
			} else if (arg1.equals("update")) {
				//修改战队名
				updateWarTeam(argsList,player);
			}else if (arg1.equals("out") && argsList.size() == 2) {
				//踢出战队
				kickOutPlayer(argsList,player);
			}else if (arg1.equals("reload")) {
				if (!op) {
					player.sendMessage("§c无权限");
				}
				//重载插件
				WarTeam.loadPlugin(player);
			}
		}
		return true;
	}

	private void warTeamList ( List<String> argsList,Player player) {
		if (argsList.size() > 2){
			player.sendMessage("§c指令不正确");
			return;
		}
		Integer count = WarTeamDatabaseHandler.selectTeamCount();
		int j = count / 5;
		int i = count % 5;
		Integer countnum;
		if (i == 0) {
			countnum = j;
		} else {
			countnum = j + 1;
		}
		if (count < 5) {
			countnum = 1;
		}
		if (argsList.size() == 1) {
			if (1 > countnum) {
				player.sendMessage(Message.no_more_team);
				return;
			}
			//查询第一页
			List<WarTeamEntity> warTeamEntities = WarTeamDatabaseHandler.selectWarTeamDataNum(0);
			player.sendMessage("§c§l§m §c§l§m §c§l§m §c§l§m §6§l§m §6§l§m §6§l§m §6§l§m §e§l§m §e§l§m §e§l§m §e§l§m §a§l§m §a§l§m §a§l§m §a§l§m §b§l§m §b§l§m §b§l§m §e【§a§l战队列表§e】 §b§l§m §b§l§m §b§l§m §a§l§m §a§l§m §a§l§m §a§l§m §e§l§m §e§l§m §e§l§m §e§l§m §6§l§m §6§l§m §6§l§m §6§l§m §c§l§m §c§l§m §c§l§m §c§l§m");
			for (WarTeamEntity warTeamEntity : warTeamEntities) {
				String name = warTeamEntity.getName();
				String creator = warTeamEntity.getCreator();
				Integer level = warTeamEntity.getLevel();
				player.sendMessage("§6●§3战队名:§a" + name + "  §3创建人:§a" + creator + "  §3等级:§6" + level);
			}
			player.sendMessage("§c§l§m §c§l§m §c§l§m §c§l§m §6§l§m §6§l§m §6§l§m §6§l§m §e§l§m §e§l§m §e§l§m §e§l§m §a§l§m §a§l§m §a§l§m §a§l§m §b§l§m §b§l§m §b§l§m §a§l<页码>  " + 1 + "\\" + countnum + " §b§l§m §b§l§m §b§l§m §a§l§m §a§l§m §a§l§m §a§l§m §e§l§m §e§l§m §e§l§m §e§l§m §6§l§m §6§l§m §6§l§m §6§l§m §c§l§m §c§l§m §c§l§m §c§l§m");
			return;
		}
		if (argsList.size() == 2) {
			//获取页数
			String num = argsList.get(1);
			Integer limit = 0;
			try {
				limit = Integer.valueOf(num);
			} catch (Exception e) {
				player.sendMessage("§c页码必须是整数");
				return;
			}
			if (limit > countnum) {
				player.sendMessage(Message.no_more_team);
				return;
			}
			limit = (limit - 1) * 5;
			List<WarTeamEntity> warTeamEntities = WarTeamDatabaseHandler.selectWarTeamDataNum(limit);
			player.sendMessage("§c§l§m §c§l§m §c§l§m §c§l§m §6§l§m §6§l§m §6§l§m §6§l§m §e§l§m §e§l§m §e§l§m §e§l§m §a§l§m §a§l§m §a§l§m §a§l§m §b§l§m §b§l§m §b§l§m §e【§a§l战队列表§e】 §b§l§m §b§l§m §b§l§m §a§l§m §a§l§m §a§l§m §a§l§m §e§l§m §e§l§m §e§l§m §e§l§m §6§l§m §6§l§m §6§l§m §6§l§m §c§l§m §c§l§m §c§l§m §c§l§m");
			for (WarTeamEntity warTeamEntity : warTeamEntities) {
				String name = warTeamEntity.getName();
				String creator = warTeamEntity.getCreator();
				Integer level = warTeamEntity.getLevel();
				player.sendMessage("§6●§3战队名:§a" + name + "  §3创建人:§a" + creator + "  §3等级:§6" + level);
			}
			player.sendMessage("§c§l§m §c§l§m §c§l§m §c§l§m §6§l§m §6§l§m §6§l§m §6§l§m §e§l§m §e§l§m §e§l§m §e§l§m §a§l§m §a§l§m §a§l§m §a§l§m §b§l§m §b§l§m §b§l§m §a§l<页码>  " + num + "\\" + countnum + " §b§l§m §b§l§m §b§l§m §a§l§m §a§l§m §a§l§m §a§l§m §e§l§m §e§l§m §e§l§m §e§l§m §6§l§m §6§l§m §6§l§m §6§l§m §c§l§m §c§l§m §c§l§m §c§l§m");
			return;
		}
	}
	private void createWarTeam ( List<String> argsList,Player player) {
		if (argsList.size() != 2){
			player.sendMessage("§c指令不正确");
			return;
		}
		String teamName = argsList.get(1);
		WarTeamEntity warTeamEntity = WarTeamDatabaseHandler.selectWarTeamByName(teamName);
		if (warTeamEntity.getId() != null) {
			player.sendMessage(Message.create_failure);
			return;
		}
		WarTeamEntity warTeamEntity2 = WarTeamDatabaseHandler.selectWarTeamByCreator(player.getName());
		if (warTeamEntity2.getId() != null) {
			player.sendMessage(Message.team_already_exist);
			return;
		}
		double money = VaultAPI.getMoney(player.getName());
		Double amount = new Double(WarTeam.getSystemConfig().getString("WarTeam.cost"));
		if (money < amount) {
			player.sendMessage(WarTeam.getSystemConfig().getString("WarTeam.prefix") +"§c您没有足够的金币！");
			return;
		}

		warTeamEntity.setName(teamName);
		warTeamEntity.setLevel(1);
		//一级最大成员数
		warTeamEntity.setMaxMember(maxNumMap.get(1));
		warTeamEntity.setUid(player.getUniqueId().toString());
		warTeamEntity.setCreator(player.getName());
		warTeamEntity.setExpAll(0);
		//升二级所需经验
		warTeamEntity.setExpCurrent(levelMap.get(2));
		warTeamEntity.setStatus(1);
		warTeamEntity.setCreated(new Date());
		warTeamEntity.setModified(new Date());
		int res = WarTeamDatabaseHandler.insert(warTeamEntity);
		WarTeamEntity warTeamEntity3 = WarTeamDatabaseHandler.selectWarTeamByName(teamName);
		WarTeamMemberEntity warTeamMemberEntity = new WarTeamMemberEntity();
		warTeamMemberEntity.setWarTeamId(warTeamEntity3.getId());
		warTeamMemberEntity.setWarTeamName(warTeamEntity3.getName());
		warTeamMemberEntity.setExp(0);
		warTeamMemberEntity.setUid(player.getName());
		warTeamMemberEntity.setStatus(1);
		warTeamMemberEntity.setCreated(new Date());
		WarTeamMemBerDatabaseHandler.insert(warTeamMemberEntity);
		player.sendMessage(Message.create_successful);
		VaultAPI.removeMoney(player.getName(),amount);
	}

	private void updateWarTeam ( List<String> argsList,Player player) {
		if (argsList.size() != 2) {
			player.sendMessage("§c指令不正确");
			return;
		}
		WarTeamMemberEntity warTeamMemberEntity1 = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(player.getName());
		if (warTeamMemberEntity1.getId() == null) {
			player.sendMessage(Message.not_join_oneTeam);
			return;
		}
		WarTeamEntity warTeamEntity2 = WarTeamDatabaseHandler.selectWarTeamByCreator(player.getName());
		if (warTeamEntity2.getId() == null) {
			player.sendMessage(Message.no_permission);
			return;
		}
		String teamName = argsList.get(1);
		WarTeamEntity warTeamEntity = WarTeamDatabaseHandler.selectWarTeamByName(teamName);
		if (warTeamEntity.getId() != null) {
			player.sendMessage(Message.update_failure);
			return;
		}
		double money = VaultAPI.getMoney(player.getName());
		Double amount = new Double(WarTeam.getSystemConfig().getString("WarTeam.update_cost"));
		if (money < amount) {
			player.sendMessage(WarTeam.getSystemConfig().getString("WarTeam.prefix") + "§c您没有足够的金币！");
			return;
		}
		WarTeamEntity warTeam = new WarTeamEntity();
		warTeam.setName(teamName);
		WarTeamDatabaseHandler.updateUserConfigDataNum(warTeamEntity2.getId(),warTeam);

		List<WarTeamMemberEntity> warTeamMemberEntities = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByWarTeamId(warTeamEntity2.getId());
		for (WarTeamMemberEntity warTeamMemberEntity : warTeamMemberEntities) {
			warTeamMemberEntity.setWarTeamName(teamName);
			WarTeamMemBerDatabaseHandler.updateUserConfigDataNum(warTeamMemberEntity.getId(),warTeamMemberEntity);
		}

		player.sendMessage(Message.update_successful);
		VaultAPI.removeMoney(player.getName(),amount);
	}

	private void kickOutPlayer ( List<String> argsList,Player player) {
		if (argsList.size() != 2) {
			player.sendMessage("§c指令不正确");
			return;
		}
		WarTeamMemberEntity warTeamMemberEntity1 = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(player.getName());
		if (warTeamMemberEntity1.getId() == null) {
			player.sendMessage(Message.not_join_oneTeam);
			return;
		}
		WarTeamEntity warTeamEntity2 = WarTeamDatabaseHandler.selectWarTeamByCreator(player.getName());
		if (warTeamEntity2.getId() == null) {
			player.sendMessage(Message.no_permission);
			return;
		}
		String playerName = argsList.get(1);
		WarTeamEntity warTeamEntity = WarTeamDatabaseHandler.selectWarTeamByCreator(playerName);
		if (warTeamEntity.getId() != null) {
			player.sendMessage(Message.not_allow_out_team);
			return;
		}

		WarTeamMemberEntity warTeamMemberEntity = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(playerName);
		if (warTeamMemberEntity.getId() == null) {
			player.sendMessage(Message.plyer_not_join_oneTeam);
			return;
		}
		WarTeamMemBerDatabaseHandler.deleteById(playerName);
		player.sendMessage(Message.out_player_successful);
	}

	private void joinWarTeam ( List<String> argsList,Player player) {
		if (argsList.size() != 2){
			player.sendMessage("§c指令不正确");
			return;
		}
		String teamName = argsList.get(1);
		WarTeamMemberEntity warTeamMemberEntity = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(player.getName());
		if (warTeamMemberEntity.getId() != null) {
			player.sendMessage(Message.apply_join2+warTeamMemberEntity.getWarTeamName());
			return;
		}
		WarTeamEntity warTeamEntity = WarTeamDatabaseHandler.selectWarTeamByName(teamName);
		if (warTeamEntity.getId() == null) {
			player.sendMessage(Message.team_inexistence);
			return;
		}
		Integer count = WarTeamMemBerDatabaseHandler.selectCount(warTeamEntity.getId());
		if (warTeamEntity.getMaxMember().equals(count)) {
			player.sendMessage(Message.team_full);
			return;
		}
		if (player.getName().equals(warTeamEntity.getCreator())) {
			player.sendMessage(Message.cant_join_your_team);
			return;
		}
		WarTeamApplyEntity warTeamApplyEntity = WarTeamApplyDatabaseHandler.selectWarTeamApplyByUid(player.getName(), teamName);
		if (warTeamApplyEntity.getId() != null) {
			player.sendMessage(Message.apply_failure);
			return;
		}
		warTeamApplyEntity.setUid(player.getName());
		warTeamApplyEntity.setWarTeamId(warTeamEntity.getId());
		warTeamApplyEntity.setWarTeamName(warTeamEntity.getName());
		warTeamApplyEntity.setApply(warTeamEntity.getCreator());
		warTeamApplyEntity.setStatus(0);
		warTeamApplyEntity.setCreated(new Date());
		WarTeamApplyDatabaseHandler.insert(warTeamApplyEntity);
		player.sendMessage(Message.apply_successful);
	}

	private void dismissWarTeam ( List<String> argsList,Player player) {
		if (argsList.size() > 1){
			player.sendMessage("§c指令不正确");
			return;
		}
		WarTeamEntity warTeamEntity = WarTeamDatabaseHandler.selectWarTeamByCreator(player.getName());
		if (warTeamEntity.getId() == null) {
			player.sendMessage(Message.no_team);
			return;
		}
		WarTeamDatabaseHandler.deleteById(warTeamEntity.getId());
		List<WarTeamMemberEntity> warTeamMemberEntities = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByWarTeamId(warTeamEntity.getId());
		for (WarTeamMemberEntity warTeamMemberEntity : warTeamMemberEntities) {
			String uid = warTeamMemberEntity.getUid();
			Player addressee = Bukkit.getServer().getPlayer(uid);
			if (addressee != null) {
				addressee.sendMessage(Message.team_dissolve);
			}
		}
		WarTeamMemBerDatabaseHandler.deleteAll(warTeamEntity.getId());
		WarTeamApplyDatabaseHandler.deleteAll(warTeamEntity.getId());
	}

	private void outWarTeam ( List<String> argsList,Player player) {
		if (argsList.size() > 1){
			player.sendMessage("§c指令不正确");
			return;
		}
		WarTeamEntity warTeamEntity = WarTeamDatabaseHandler.selectWarTeamByCreator(player.getName());
		if (warTeamEntity.getId() != null) {
			player.sendMessage(Message.not_allow_out_team);
			return;
		}

		WarTeamMemberEntity warTeamMemberEntity = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(player.getName());
		if (warTeamMemberEntity.getId() == null) {
			player.sendMessage(Message.not_join_oneTeam);
			return;
		}
		WarTeamMemBerDatabaseHandler.deleteById(player.getName());
		player.sendMessage(Message.out_successful);
	}

	private void applyPassWarTeam ( List<String> argsList,Player player) {
		if (argsList.size() != 2){
			player.sendMessage("§c指令不正确");
			return;
		}
		String userName = argsList.get(1);
		WarTeamEntity warTeamEntity1 = WarTeamDatabaseHandler.selectWarTeamByCreator(player.getName());
		if (warTeamEntity1.getId() == null) {
			player.sendMessage(Message.create_first);
			return;
		}
		WarTeamApplyEntity warTeamApplyEntity = WarTeamApplyDatabaseHandler.selectWarTeamApplyByUidAndApply(userName,player.getName());
		if (warTeamApplyEntity.getId() == null) {
			player.sendMessage(Message.apply_inexistence);
			return;
		}
		WarTeamMemberEntity warTeamMemberEntity1 = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(userName);
		if (warTeamMemberEntity1.getId() != null) {
			player.sendMessage(Message.apply_join);
			//玩家加入其他战队后，此申请自动拒绝
			warTeamApplyEntity.setStatus(2);
			WarTeamApplyDatabaseHandler.updateUserConfigDataNum(warTeamApplyEntity.getId(),warTeamApplyEntity);
			return;
		}
		WarTeamEntity warTeamEntity = WarTeamDatabaseHandler.selectWarTeamByName(warTeamApplyEntity.getWarTeamName());

		WarTeamMemberEntity warTeamMemberEntity = new WarTeamMemberEntity();
		warTeamMemberEntity.setWarTeamId(warTeamEntity.getId());
		warTeamMemberEntity.setWarTeamName(warTeamEntity.getName());
		warTeamMemberEntity.setExp(0);
		warTeamMemberEntity.setUid(warTeamApplyEntity.getUid());
		warTeamMemberEntity.setStatus(1);
		warTeamMemberEntity.setCreated(new Date());
		WarTeamMemBerDatabaseHandler.insert(warTeamMemberEntity);
		warTeamEntity.setMaxMember(warTeamEntity.getMaxMember() + 1);
		WarTeamDatabaseHandler.updateUserConfigDataNum(warTeamEntity.getId(),warTeamEntity);
		warTeamApplyEntity.setStatus(1);
		WarTeamApplyDatabaseHandler.updateUserConfigDataNum(warTeamApplyEntity.getId(),warTeamApplyEntity);
		Player addressee = Bukkit.getServer().getPlayer(userName);
		if (addressee != null) {
			addressee.sendMessage(Message.join_successful);
		}
		player.sendMessage(Message.apply_pass);
	}

	private void applyNoPassWarTeam ( List<String> argsList,Player player) {
		if (argsList.size() != 2){
			player.sendMessage("§c指令不正确");
			return;
		}
		String userName = argsList.get(1);
		WarTeamEntity warTeamEntity1 = WarTeamDatabaseHandler.selectWarTeamByCreator(player.getName());
		if (warTeamEntity1.getId() == null) {
			player.sendMessage(Message.create_first);
			return;
		}
		WarTeamApplyEntity warTeamApplyEntity = WarTeamApplyDatabaseHandler.selectWarTeamApplyByUidAndApply(userName,player.getName());
		if (warTeamApplyEntity.getId() == null) {
			player.sendMessage(Message.apply_inexistence);
			return;
		}
		WarTeamMemberEntity warTeamMemberEntity1 = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(userName);
		if (warTeamMemberEntity1.getId() != null) {
			player.sendMessage(Message.apply_join);
			//玩家加入其他战队后，此申请自动拒绝
			warTeamApplyEntity.setStatus(2);
			WarTeamApplyDatabaseHandler.updateUserConfigDataNum(warTeamApplyEntity.getId(),warTeamApplyEntity);
			return;
		}
		warTeamApplyEntity.setStatus(2);
		WarTeamApplyDatabaseHandler.updateUserConfigDataNum(warTeamApplyEntity.getId(),warTeamApplyEntity);
		Player addressee = Bukkit.getServer().getPlayer(userName);
		if (addressee != null) {
			addressee.sendMessage(Message.join_failure);
		}
		player.sendMessage(Message.apply_nopass);
	}
}
