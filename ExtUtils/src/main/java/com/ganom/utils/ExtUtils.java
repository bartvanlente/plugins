/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package com.ganom.utils;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.queries.DecorativeObjectQuery;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.queries.GroundObjectQuery;
import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.queries.WallObjectQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "ExtUtils"
)
@Slf4j
@SuppressWarnings("unused")
@Singleton
public class ExtUtils extends Plugin
{
	@Inject
	private Client client;

	@Override
	protected void startUp()
	{

	}

	@Override
	protected void shutDown()
	{

	}

	public int[] stringToIntArray(String string)
	{
		return Arrays.stream(string.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
	}

	@Nullable
	public GameObject findNearestGameObject(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GameObjectQuery()
			.idEquals(ids)
			.result(client)
			.nearestTo(client.getLocalPlayer());
	}

	@Nullable
	public WallObject findNearestWallObject(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return null;
		}

		return new WallObjectQuery()
			.idEquals(ids)
			.result(client)
			.nearestTo(client.getLocalPlayer());
	}

	@Nullable
	public DecorativeObject findNearestDecorObject(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return null;
		}

		return new DecorativeObjectQuery()
			.idEquals(ids)
			.result(client)
			.nearestTo(client.getLocalPlayer());
	}

	@Nullable
	public GroundObject findNearestGroundObject(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GroundObjectQuery()
			.idEquals(ids)
			.result(client)
			.nearestTo(client.getLocalPlayer());
	}

	public List<GameObject> getGameObjects(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GameObjectQuery()
			.idEquals(ids)
			.result(client)
			.list;
	}

	public List<WallObject> getWallObjects(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new WallObjectQuery()
			.idEquals(ids)
			.result(client)
			.list;
	}

	public List<DecorativeObject> getDecorObjects(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new DecorativeObjectQuery()
			.idEquals(ids)
			.result(client)
			.list;
	}

	public List<GroundObject> getGroundObjects(int... ids)
	{
		assert client.isClientThread();

		if (client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GroundObjectQuery()
			.idEquals(ids)
			.result(client)
			.list;
	}

	@Nullable
	public TileObject findNearestObject(int... ids)
	{
		GameObject gameObject = findNearestGameObject(ids);

		if (gameObject != null)
		{
			return gameObject;
		}

		WallObject wallObject = findNearestWallObject(ids);

		if (wallObject != null)
		{
			return wallObject;
		}
		DecorativeObject decorativeObject = findNearestDecorObject(ids);

		if (decorativeObject != null)
		{
			return decorativeObject;
		}

		return findNearestGroundObject(ids);
	}

	public List<WidgetItem> getItems(int... itemIDs)
	{
		assert client.isClientThread();

		return new InventoryWidgetItemQuery()
			.idEquals(itemIDs)
			.result(client)
			.list;
	}

	public List<Widget> getEquippedItems(int[] itemIds)
	{
		assert client.isClientThread();

		Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);

		List<Integer> equippedIds = new ArrayList<>();

		for (int i : itemIds)
		{
			equippedIds.add(i);
		}

		List<Widget> equipped = new ArrayList<>();

		if (equipmentWidget.getStaticChildren() != null)
		{
			for (Widget widgets : equipmentWidget.getStaticChildren())
			{
				for (Widget items : widgets.getDynamicChildren())
				{
					if (equippedIds.contains(items.getItemId()))
					{
						equipped.add(items);
					}
				}
			}
		}
		else
		{
			log.error("Children is Null!");
		}

		return equipped;
	}

	public int getTabHotkey(Tab tab)
	{
		assert client.isClientThread();

		final int var = client.getVarbitValue(client.getVarps(), tab.getVarbit());
		final int offset = 111;

		switch (var)
		{
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				return var + offset;
			case 13:
				return 27;
			default:
				return -1;
		}
	}

	public WidgetInfo getSpellWidgetInfo(String spell)
	{
		assert client.isClientThread();
		return Spells.getWidget(spell);
	}

	public WidgetInfo getPrayerWidgetInfo(String spell)
	{
		assert client.isClientThread();
		return PrayerMap.getWidget(spell);
	}

	public Widget getSpellWidget(String spell)
	{
		assert client.isClientThread();
		return client.getWidget(Spells.getWidget(spell));
	}

	public Widget getPrayerWidget(String spell)
	{
		assert client.isClientThread();
		return client.getWidget(PrayerMap.getWidget(spell));
	}

	/**
	 * This method must be called on a new
	 * thread, if you try to call it on
	 * {@link net.runelite.client.callback.ClientThread}
	 * it will result in a crash/desynced thread.
	 */
	public void click(Rectangle rectangle)
	{
		assert !client.isClientThread();

		Point p = getClickPoint(rectangle);

		if (p.getX() < 1 || p.getY() < 1)
		{
			return;
		}

		click(p).run();
	}

	private Runnable click(Point p)
	{
		return () ->
		{
			assert !client.isClientThread();

			if (client.isStretchedEnabled())
			{
				double scale = client.getScalingFactor();
				final int x = (int) (p.getX() * scale);
				final int y = (int) (p.getY() * scale);
				final Point click = new Point(x, y);

				eventDispatcher(501, click);
				eventDispatcher(502, click);
				eventDispatcher(500, click);
				return;
			}
			eventDispatcher(501, p);
			eventDispatcher(502, p);
			eventDispatcher(500, p);
		};
	}

	public Point getClickPoint(Rectangle rect)
	{
		final int x = (int) (rect.getX() + getRandomIntBetweenRange((int) rect.getWidth() / 6 * -1, (int) rect.getWidth() / 6) + rect.getWidth() / 2);
		final int y = (int) (rect.getY() + getRandomIntBetweenRange((int) rect.getHeight() / 6 * -1, (int) rect.getHeight() / 6) + rect.getHeight() / 2);

		return new Point(x, y);
	}

	public int getRandomIntBetweenRange(int min, int max)
	{
		return (int) ((Math.random() * ((max - min) + 1)) + min);
	}

	private void eventDispatcher(int id, Point point)
	{
		MouseEvent e = new MouseEvent(
			client.getCanvas(),
			id,
			System.currentTimeMillis(),
			0,
			point.getX(),
			point.getY(),
			1,
			false,
			1
		);

		client.getCanvas().dispatchEvent(e);
	}
}
