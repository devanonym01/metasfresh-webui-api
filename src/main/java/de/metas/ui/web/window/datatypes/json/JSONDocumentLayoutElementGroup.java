package de.metas.ui.web.window.datatypes.json;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

import de.metas.ui.web.window.descriptor.DocumentLayoutElementGroupDescriptor;
import de.metas.util.GuavaCollectors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;

/*
 * #%L
 * metasfresh-webui-api
 * %%
 * Copyright (C) 2016 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

@ApiModel("elementGroup")
@SuppressWarnings("serial")
public final class JSONDocumentLayoutElementGroup implements Serializable
{
	static List<JSONDocumentLayoutElementGroup> ofList(
			@NonNull final List<DocumentLayoutElementGroupDescriptor> elementGroups,
			@NonNull final JSONOptions jsonOpts)
	{
		return elementGroups.stream()
				.map(elementGroup -> of(elementGroup, jsonOpts))
				.filter(group -> !group.isEmpty())
				.collect(GuavaCollectors.toImmutableList());
	}

	public static JSONDocumentLayoutElementGroup of(
			@NonNull final DocumentLayoutElementGroupDescriptor elementGroup,
			@NonNull final JSONOptions jsonOpts)
	{
		return new JSONDocumentLayoutElementGroup(elementGroup, jsonOpts);
	}

	@ApiModelProperty(allowEmptyValue = true)
	@JsonProperty("type")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Getter
	private final JSONLayoutType type;

	@ApiModelProperty( //
			allowEmptyValue = true, value = "Number of equal-width-columns into which the included elementsLines shall be displayed:\n"
					+ "Notes:\n"
					+ "* one element line per cell"
					+ "* an empty element line shall be rendered as empty cell"
					+ "* if you have e.g. columnCount=3 and four element lines, then the rightmost two cells of the last line shall be empty"
					+ "* if this property is missing, then <code>1</code> should be assumed")
	@JsonProperty("columnCount")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Getter
	private final Integer columnCount;

	@JsonProperty("internalName")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Getter
	private final String internalName;

	@ApiModelProperty( //
			allowEmptyValue = true, value = "Container for elementy that are supposed to be displayed next to each other\n"
					+ "Notes:"
					+ "* individual element lines might be empty for layout purposes; see <code>columnCount</code>\n"
					+ "* in most of the cases, each elementLine has one element")
	@JsonProperty("elementsLine")
	@JsonInclude(JsonInclude.Include.ALWAYS)
	@Getter
	private final List<JSONDocumentLayoutElementLine> elementLines;

	private JSONDocumentLayoutElementGroup(
			@NonNull final DocumentLayoutElementGroupDescriptor elementGroup,
			@NonNull final JSONOptions jsonOpts)
	{
		this.type = JSONLayoutType.fromNullable(elementGroup.getLayoutType());
		this.columnCount = elementGroup.getColumnCount();
		this.internalName = elementGroup.getInternalName();
		this.elementLines = JSONDocumentLayoutElementLine.ofList(elementGroup.getElementLines(), jsonOpts);
	}

	@JsonCreator
	private JSONDocumentLayoutElementGroup(
			@JsonProperty("type") final JSONLayoutType type,
			@JsonProperty("columnCount") @Nullable final Integer columnCount,
			@JsonProperty("internalName") @Nullable final String internalName,
			@JsonProperty("elementsLine") @Nullable final List<JSONDocumentLayoutElementLine> elementLines)
	{
		this.type = type;
		this.columnCount = columnCount;
		this.internalName = internalName;
		this.elementLines = elementLines == null ? ImmutableList.of() : ImmutableList.copyOf(elementLines);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("type", type)
				.add("columnCount", columnCount)
				.add("elements", elementLines.isEmpty() ? null : elementLines)
				.toString();
	}

	private boolean isEmpty()
	{
		final boolean atLeastOneLineIsFilled = elementLines
				.stream()
				.anyMatch(line -> !line.isEmpty());
		return !atLeastOneLineIsFilled;
	}
}
