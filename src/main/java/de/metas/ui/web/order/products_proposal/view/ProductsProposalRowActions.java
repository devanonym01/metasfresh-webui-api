package de.metas.ui.web.order.products_proposal.view;

import java.util.List;
import java.util.Optional;

import de.metas.ui.web.order.products_proposal.view.ProductsProposalRowChangeRequest.ProductsProposalRowChangeRequestBuilder;
import de.metas.ui.web.window.datatypes.json.JSONDocumentChangedEvent;
import lombok.experimental.UtilityClass;

/*
 * #%L
 * metasfresh-webui-api
 * %%
 * Copyright (C) 2019 metas GmbH
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

@UtilityClass
public class ProductsProposalRowActions
{
	public static ProductsProposalRowChangeRequest toChangeRequest(final List<JSONDocumentChangedEvent> fieldChangeRequests)
	{
		final ProductsProposalRowChangeRequestBuilder builder = ProductsProposalRowChangeRequest.builder();
		for (final JSONDocumentChangedEvent fieldChangeRequest : fieldChangeRequests)
		{
			final String fieldName = fieldChangeRequest.getPath();
			if (ProductsProposalRow.FIELD_Qty.equals(fieldName))
			{
				builder.qty(Optional.ofNullable(fieldChangeRequest.getValueAsBigDecimal()));
			}
		}

		return builder.build();
	}

}