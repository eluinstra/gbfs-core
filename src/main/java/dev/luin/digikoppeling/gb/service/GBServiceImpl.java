/**
 * Copyright 2020 E.Luinstra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.luin.digikoppeling.gb.service;

import org.springframework.transaction.annotation.Transactional;

import dev.luin.digikoppeling.gb.common.ExternalDataReferenceBuilder;
import dev.luin.fs.core.file.FileSystem;
import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.val;
import lombok.experimental.FieldDefaults;
import nl.logius.digikoppeling.gb._2010._10.ExternalDataReference;

@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
@AllArgsConstructor
@Transactional(transactionManager = "dataSourceTransactionManager")
public class GBServiceImpl implements GBService
{
	@NonNull
	FileSystem fileSystem;
	@NonNull
	ExternalDataReferenceBuilder externalDataReferenceBuilder;

	@Override
	public ExternalDataReference getExternalDataReference(String...paths) throws GBServiceException
	{
		val files = List.of(paths)
				//.map(p -> fileService.getFile(path).orElseThrow(() -> new GBServiceException(p + " not found!")))
				.map(p -> fileSystem.findFile(p).<GBServiceException>getOrElseThrow(() -> new GBServiceException(p + " not found!")));
		return externalDataReferenceBuilder.build(files);
	}
}